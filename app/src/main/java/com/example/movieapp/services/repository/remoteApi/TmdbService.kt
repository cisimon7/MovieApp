@file:Suppress("ParameterName", "FunctionName")

package com.example.movieapp.services.repository.remoteApi

import android.util.Log
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.MovieBrief
import com.example.movieapp.services.model.MovieDetailed
import com.example.movieapp.services.repository.QueryStringDSL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

abstract class TmdbService(
    private val key: String,
    private val json: Json,
    val httpClient: HttpClient
) {

    private val baseUrl = "https://api.themoviedb.org/3"
    private lateinit var profileUrl: String
    private lateinit var backdropUrl: String

    private suspend fun getImageConfigurations() {
        val url = "$baseUrl/configuration?api_key=$key"
        val response: HttpResponse =
            httpClient.get(url) //httpClient.use { client -> client.get(url) }
        val imageConfig = json.parseToJsonElement(response.receive()).jsonObject["images"]

        val secureBaseUrl = imageConfig?.jsonObject?.get("secure_base_url")?.let {
            json.decodeFromJsonElement(String.serializer(), it)
        }
        val backdropSizes = imageConfig?.jsonObject?.get("backdrop_sizes")?.let {
            json.decodeFromJsonElement(ListSerializer(String.serializer()), it)
        }
        val profileSizes = imageConfig?.jsonObject?.get("profile_sizes")?.let {
            json.decodeFromJsonElement(ListSerializer(String.serializer()), it)
        }

        profileUrl = "$secureBaseUrl/${profileSizes!!.last()}"
        backdropUrl = "$secureBaseUrl/${backdropSizes!!.last()}"
    }

    private suspend fun getImageUrl(imageType: ImageType, imageUri: String): String {
        if (!::profileUrl.isInitialized || !::backdropUrl.isInitialized) {
            getImageConfigurations()
        }

        return when (imageType) {
            is ImageType.ProfileImage -> "$profileUrl/$imageUri"
            is ImageType.BackdropImage -> "$backdropUrl/$imageUri"
        }
    }

    open suspend fun getMovieDetailsById(movieId: Int): Movie {

        val response: HttpResponse = httpClient.get("$baseUrl/movie/$movieId?api_key=$key")

        val movieDetailed = response.receive<MovieDetailed>()

        return movieDetailed.format().copy(
            cover_url = getImageUrl(ImageType.ProfileImage, movieDetailed.poster_path),
            backdrop_url = getImageUrl(ImageType.BackdropImage, movieDetailed.backdrop_path)
        ).let { Log.i("CISIMON7", it.toString()); it }
    }

    open suspend fun discoverMovies(queryParameters: QueryStringDSL): List<Movie> {

        val queryString = queryParameters.toString()
        val url = "$baseUrl/discover/movie?api_key=${key}${queryString}"
        val response: HttpResponse =
            httpClient.get(url) //httpClient.use { client -> client.get(url) }

        val body: JsonElement = json.parseToJsonElement(response.receive())
        val resultJson: JsonElement? = body.jsonObject["results"]

        val newMovieBreves: List<MovieBrief> = resultJson
            ?.let { result ->
                json.decodeFromJsonElement(ListSerializer(MovieBrief.serializer()), result)
            } ?: emptyList()

        return newMovieBreves.map { movie_brief ->
            movie_brief.format().copy(
                cover_url = getImageUrl(ImageType.ProfileImage, movie_brief.poster_path)
            )
        }
    }

    companion object {
        const val items_per_page = 30
    }
}

sealed interface ImageType {
    object ProfileImage : ImageType
    object BackdropImage : ImageType
}