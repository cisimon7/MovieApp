package com.example.movieapp.services.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import com.example.movieapp.services.repository.localDb.TmdbRemoteMediator
import com.example.movieapp.services.repository.remoteApi.TmdbService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MovieRepository(
    private val tmdbService: TmdbService,
    private val movieRoomDb: MovieRoomDatabase
) {

    val connectionState: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.Unavailable)

    suspend fun getMovieDetails(movieId: Int): Movie {
        runCatching { tmdbService.getMovieDetailsById(movieId) }
            .onSuccess { movie ->
                val movieFromDb = movieRoomDb.movieDao().getMovieDetailsById(movieId)
                movieFromDb.update(movie)
                movieRoomDb.movieDao().updateMovie(movieFromDb)
            }.onFailure { error ->
                Log.e("MovieAppErrorMessage", "Error: ${error.message ?: "error"}")
            }

        /* Database is the single source of truth */
        return movieRoomDb.movieDao().getMovieDetailsById(movieId)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun discoverMovies(query_params: QueryStringDSL): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = (TmdbService.items_per_page * 0.5 / 3).toInt(),
                enablePlaceholders = false
            ),
            remoteMediator = TmdbRemoteMediator(query_params, tmdbService, movieRoomDb),
            pagingSourceFactory = { movieRoomDb.movieDao().discoverMovies() }
        ).flow
    }

    fun tearDown() {
        tmdbService.httpClient.close()
        movieRoomDb.close()
    }
}