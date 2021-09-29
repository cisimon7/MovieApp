package com.example.movieapp.services.repository.localDb

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.TmdbService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalPagingApi::class, ExperimentalTime::class)
class TmdbRemoteMediator(
    private val query: QueryStringDSL,
    private val tmdbService: TmdbService,
    private val movieRoomDb: MovieRoomDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = Duration.minutes(1)
        return if (Clock.System.now() - MovieRoomDatabase.lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Log.i("CISIMON7","Cache Timeout, fetching new data")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {

        val loadKey = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                key += 1
                key
            }
        }

        return runCatching { tmdbService.discoverMovies(query) }.fold(
            onSuccess = { receivedMovies ->
                movieRoomDb.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieRoomDb.clearAllTables()
                    }
                    movieRoomDb.movieDao().insertMovieList(receivedMovies)
                }
                MediatorResult.Success(endOfPaginationReached = query.page==loadKey)
            },
            onFailure = { error -> MediatorResult.Error(error) }
        )
    }

    companion object {
        private var key = 0
    }
}