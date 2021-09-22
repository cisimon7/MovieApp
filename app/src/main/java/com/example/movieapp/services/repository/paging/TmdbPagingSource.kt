package com.example.movieapp.services.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.TmdbService

private const val StartingIndex = 1

class TmdbPagingSource(private val tmdbService: TmdbService, private val query: QueryStringDSL) :
    PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: StartingIndex

        return runCatching { tmdbService.discoverMovies(query) }.fold(
            onSuccess = { receivedMovies ->
                val nextKey: Int? = query.page?.plus(1)
                    /*if (receivedMovies.isEmpty()) null
                    else position + (params.loadSize / TmdbService.items_per_page)*/

                LoadResult.Page(
                    data = receivedMovies,
                    prevKey = if (position == StartingIndex) null else position - 1,
                    nextKey = nextKey
                )
            },
            onFailure = { error -> LoadResult.Error(error) }
        )
    }
}

/*
when (error) {
    is RedirectResponseException -> LoadResult.Error(error)
    is ClientRequestException -> LoadResult.Error(error)
    is ServerResponseException -> LoadResult.Error(error)
    else -> { throw error }
}*/
