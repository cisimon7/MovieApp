package com.example.movieapp.viewModel

import com.example.movieapp.di.appModule
import com.example.movieapp.di.viewModelModule
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import com.example.movieapp.services.repository.remoteApi.TmdbService
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.testModules(database: MovieRoomDatabase) = modules(appModule, viewModelModule,
    module {
        single { database }
        single<TmdbService> {
            object : TmdbService(key, json, get()) {

                override suspend fun getMovieDetailsById(movieId: Int): Movie {
                    return httpClient.get("https://getMovie")
                }

                override suspend fun discoverMovies(queryParameters: QueryStringDSL): List<Movie> {
                    return httpClient.get("https://discover")
                }
            }
        }
        factory {
            HttpClient(mockEngine) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(json)
                }
            }
        }
    }
)