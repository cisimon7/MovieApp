package com.example.movieapp.viewModel

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MovieListViewModelTest : KoinTest {

    private lateinit var viewModel: MovieListViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @OptIn(InternalAPI::class)
    @Before
    fun loadKoinModulesAndViewModel() {

        stopKoin()
        val context: Context = ApplicationProvider.getApplicationContext()
        val database: MovieRoomDatabase =
            Room.inMemoryDatabaseBuilder(context, MovieRoomDatabase::class.java)
                .setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .build()

        startKoin { testModules(database) }
        viewModel = get()
    }

    @Test
    fun testMockEngine() {
        val client by inject<HttpClient>()
        runBlocking {
            val response: Movie =
                client.get("https://url")
            Log.i("CISIMON7", json.encodeToString(Movie.serializer(), response))
        }
    }

    @Test
    fun getMovie() {
        runBlocking {
            val response = viewModel.fetchMovieById(660)
            Log.i(
                "CISIMON7",
                json.encodeToString(Movie.serializer(), response.value)
            )
        }
    }

    @Test
    fun getMoviesList() {
        runBlocking {
            viewModel.fetchLatestMovies()
            /*viewModel.movieList.value.forEach { response ->
                Log.i("CISIMON7",response.movie.title)
            }*/
        }
    }
}