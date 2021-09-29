package com.example.movieapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.MovieRepository
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.SortProperty
import kotlinx.coroutines.flow.*

class MovieListViewModel(private val repository: MovieRepository) : ViewModel() {

    var connectionState: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.Unavailable)

    private var _movieList: MutableStateFlow<PagingData<Movie>> =
        MutableStateFlow(PagingData.empty())
    val movieList: StateFlow<PagingData<Movie>> = _movieList

    init {
        connectionState
            .onEach { state -> repository.connectionState.value = state }
            .launchIn(viewModelScope)
    }

    private val options: MutableStateFlow<QueryStringDSL> = MutableStateFlow(QueryStringDSL {
        language = "en-US"
        page = 1
        include_adult = false
        include_video = true
        sort_by { SortProperty.Popularity.Desc }
    })

    fun fetchLatestMovies() {
        repository.discoverMovies(options.value)
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = PagingData.empty()
            ).onEach { pagingData -> _movieList.value = pagingData }
            .launchIn(viewModelScope)
    }

    fun fetchLatestMoviesNextPage() {
        options.value.page = options.value.page.plus(1)
    }

    suspend fun fetchMovieById(movieId: Int): StateFlow<Movie> {
        return flowOf(repository.getMovieDetails(movieId)).stateIn(viewModelScope)
    }

    override fun onCleared() {
        repository.tearDown()
    }
}