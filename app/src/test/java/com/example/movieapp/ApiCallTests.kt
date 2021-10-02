package com.example.movieapp

import com.example.movieapp.services.model.Converters
import com.example.movieapp.services.model.Movie
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
import kotlinx.datetime.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.random.Random

class ApiCallTests {

    private lateinit var client: HttpClient

    private val jsonFormatter = Json {
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private val key = "5e2a1cba50d2f209f83dbd7aef1d615e"

    private val converters = Converters()

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
                ListSerializer(MovieBrief.serializer()), emptyList()
            )
        val result =
            jsonFormatter.decodeFromJsonElement(ListSerializer(MovieBrief.serializer()), resultJson)
        println(result)
    }

    @Test
    fun generateSQLQueriesForPreloadedData() = runBlocking {
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
                ListSerializer(MovieBrief.serializer()), emptyList()
            )
        val result =
            jsonFormatter.decodeFromJsonElement(ListSerializer(MovieBrief.serializer()), resultJson)

        File("src/main/assets/database/preLoadedMovies.json")
            .bufferedWriter()
            .use { out ->
            out.write(
                jsonFormatter.encodeToString(ListSerializer(Movie.serializer()),
                result.map { it.format() })
            )
        }

        File("src/main/assets/database/preLoadedMoviesQuery.txt")
            .bufferedWriter()
            .use { out ->
            out.write("INSERT INTO movie_table\n")
            out.write("\t(id, title, cover_url, tagline, overview, genres, release_date, vote_average, vote_count, backdrop_url, runtime, revenue, budget, adult, status)\n")
            out.write("VALUES\n")
            result.map { it.format() }.forEach { movie ->
                with(movie) {
                    out.write("""    ("$id", "${title.formatCharacters()}", "https://image.tmdb.org/t/p//original/$cover_url", "${tagline.formatCharacters()}", "${overview.formatCharacters()}", "$genres", "$release_date", "$vote_average", "$vote_count", "https://image.tmdb.org/t/p//original/$backdrop_url", "$runtime", "$revenue", "$budget", "$adult", "$status"),
                        |
                    """.trimMargin())
                }
            }
        }

        File("src/main/assets/database/preLoadedReminderQuery.txt")
            .bufferedWriter()
            .use { out ->
            out.write("INSERT INTO reminder_table\n")
            out.write("\t(movieId, dateTime)\n")
            out.write("VALUES\n")
            result.map { it.format() }.forEach { movie ->
                with(movie) {
                    out.write("""    ("$id", "${converters.dateTimeToString(generateRandomDateTime())}"),
                        |
                    """.trimMargin())
                }
            }
        }
    }

    private fun String.formatCharacters(): String {
        return this.replace('\"', '\'')
    }

    private fun generateRandomDateTime(): LocalDateTime {
        val tz = TimeZone.currentSystemDefault()

        return Instant.fromEpochMilliseconds(
            Random.nextLong(
                LocalDateTime(2021, 9, 27, 0, 0, 0, 0).toInstant(tz)
                    .toEpochMilliseconds(),
                LocalDateTime(2021, 12, 31, 0, 0, 0, 0).toInstant(tz)
                    .toEpochMilliseconds()
            )
        ).toLocalDateTime(tz)
    }
}

