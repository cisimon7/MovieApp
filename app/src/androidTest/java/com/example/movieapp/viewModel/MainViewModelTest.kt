package com.example.movieapp.viewModel

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.di.appModule
import com.example.movieapp.di.viewModelModule
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.services.repository.MovieApiResponse
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.TmdbService
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainViewModelTest : KoinTest {

    private lateinit var viewModel: MainViewModel

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

        startKoin {
            modules(appModule, viewModelModule,
                module {
                    single { database }
                    single<TmdbService> {
                        object : TmdbService(key, json, get(), get()) {

                            override suspend fun getMovieDetailsById(movieId: Int): Movie {
                                return httpClient.get<Movie>("https://getMovie")
                            }

                            override suspend fun discoverMovies(queryParameters: QueryStringDSL): List<Movie> {
                                return httpClient.get<List<Movie>>("https://discover")
                            }
                        }
                    }
                    factory<HttpClient> {
                        HttpClient(mockEngine) {
                            install(JsonFeature) {
                                serializer = KotlinxSerializer(json)
                            }
                        }
                    }
                }
            )
        }
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
            when (val response = viewModel.fetchMovieById(660).value) {
                is MovieApiResponse.Success -> Log.i(
                    "CISIMON7",
                    json.encodeToString(Movie.serializer(), response.movie)
                )
                else -> Log.i("CISIMON7", "Error")
            }
        }
    }

    @Test
    fun getMoviesList() {
        runBlocking {
            viewModel.fetchLatestMovies()
            viewModel.movieList.value.forEach { response ->
                when (response) {
                    is MovieApiResponse.Success -> Log.i("CISIMON7",response.movie.title)
                    else -> Log.i("CISIMON7", "Error")
                }
            }
        }
    }
}

private const val key = "5e2a1cba50d2f209f83dbd7aef1d615e"

private val json = Json {
    prettyPrint = true
    isLenient = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

private val mockEngine = MockEngine { request ->
    when {
        request.url.host.contains("discover") -> {
            respond(
                content = json.encodeToString(
                    ListSerializer(Movie.serializer()),
                    sampleMovies
                ),
                status = HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        else -> {
            respond(
                content = json.encodeToString(
                    Movie.serializer(),
                    sampleMovies.first()
                ),
                status = HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
    }
}