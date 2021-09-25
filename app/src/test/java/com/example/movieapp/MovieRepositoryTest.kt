package com.example.movieapp

import com.example.movieapp.services.repository.MovieRepository
import com.example.movieapp.services.repository.QueryStringDSL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieRepositoryTest {

    private val key = "5e2a1cba50d2f209f83dbd7aef1d615e"
    private lateinit var client: HttpClient
    private lateinit var json: Json
    private lateinit var repositoryTest: MovieRepository

    @Before
    fun setUp() {
        json = Json {
            prettyPrint = true
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        client = HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
        }
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun getMovieDetails(): Unit = runBlocking {
        val movie = repositoryTest.getMovieDetails(660)
        println(movie)
    }

    @Test
    fun getListOfMovies(): Unit = runBlocking {
        val movies = repositoryTest.discoverMovies(QueryStringDSL())
        /*println(movies.first())
        println(movies.last())*/
    }

    @Test
    fun getImageConfiguration() = runBlocking {
        val url =
            "https://api.themoviedb.org/3/configuration?api_key=$key"
        val response: HttpResponse = client.get(url)
        val body = json.parseToJsonElement(response.receive()).jsonObject["images"]

        val secure_base_url = body?.jsonObject?.get("secure_base_url")
        val backdrop_sizes = body?.jsonObject?.get("backdrop_sizes")
        val profile_sizes = body?.jsonObject?.get("profile_sizes")

        val secure = json.decodeFromJsonElement(String.serializer(), secure_base_url!!)
        val backdrop =
            json.decodeFromJsonElement(ListSerializer(String.serializer()), backdrop_sizes!!)
        val profile =
            json.decodeFromJsonElement(ListSerializer(String.serializer()), profile_sizes!!)

        println(secure)
        println(backdrop)
        println(profile)
    }

    @Test
    fun getImage() = runBlocking {
        val httpResponse: HttpResponse =
            client.get("https://image.tmdb.org/t/p/w300/yizL4cEKsVvl17Wc1mGEIrQtM2F.jpg") {
                onDownload { bytesSentTotal, contentLength ->
                    println("Received $bytesSentTotal bytes from $contentLength")
                }
            }
        val responseBody: ByteArray = httpResponse.receive()

        val fileName = "Test_download.jpg"
        /*File(fileName).writeBytes(responseBody)
        println("A file saved to ${file.path}")*/


    }
}