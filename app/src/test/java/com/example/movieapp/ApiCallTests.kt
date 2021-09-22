package com.example.movieapp

import com.example.movieapp.services.model.MovieBrief
import com.example.movieapp.services.model.MovieDetailed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApiCallTests {

    private lateinit var client: HttpClient
    private val jsonFormatter = Json {
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }
    private val key = "5e2a1cba50d2f209f83dbd7aef1d615e"

    @Before
    fun setUp() {
        client = HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(jsonFormatter)
            }
        }
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun callTest() = runBlocking {
        val response: HttpResponse = client.get("https://ktor.io/")
        println(response.status)
    }

    @Test
    fun getMovieById() = runBlocking {

        val movieId = 660

        /*https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US*/
        val response: HttpResponse =
            client.get("https://api.themoviedb.org/3/movie/$movieId?api_key=$key")
        println(response.receive<MovieDetailed>())
    }

    @Test
    fun discoverMovie() = runBlocking {
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${key}&language=en-US" +
                "&sort_by=popularity.desc" +
                "&include_adult=false" +
                "&include_video=false" +
                "&page=1" +
                "&with_watch_monetization_types=flatrate"

        val response: HttpResponse = client.get(url)
        val body: JsonElement = jsonFormatter.parseToJsonElement(response.receive())
        val resultJson = body.jsonObject["results"]
            ?: jsonFormatter.encodeToJsonElement(
                ListSerializer(
                    MovieBrief.serializer()
                ), emptyList()
            )
        val result =
            jsonFormatter.decodeFromJsonElement(ListSerializer(MovieBrief.serializer()), resultJson)
        println(result)
    }
}

