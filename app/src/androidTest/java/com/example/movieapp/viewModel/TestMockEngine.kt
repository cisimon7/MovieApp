package com.example.movieapp.viewModel

import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

const val key = "5e2a1cba50d2f209f83dbd7aef1d615e"

val json = Json {
    prettyPrint = true
    isLenient = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

val mockEngine = MockEngine { request ->
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