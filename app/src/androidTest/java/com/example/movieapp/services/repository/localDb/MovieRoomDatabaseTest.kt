package com.example.movieapp.services.repository.localDb

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.services.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MovieRoomDatabaseTest {

    private lateinit var database: MovieRoomDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var reminderDao: ReminderDao

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val json = Json { prettyPrint = true }
    private val converters = Converters()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    /* This is already inserted in the Database */
    private val testMovie = Movie(
        550988,
        "Free Guy",
        "/xmbU4JTUm8rsdtn7Y3Fcm30GpeT.jpg",
        "",
        "A bank teller called Guy realizes he is a background character in an open world video game called Free City that will soon go offline.",
        listOf("Drama"),
        converters.stringToDate("2021-08-11")!!,
        80.0F,
        1471,
        "",
        0,
        0,
        0,
        false,
        Status.Unknown
    )

    private val testReminder = Reminder(550988, converters.stringToDateTime("2021-10-24-3-33-42")!!)

    @Before
    fun createDB() {
        database =
            Room.databaseBuilder(context, MovieRoomDatabase::class.java, "test_database2")
                .allowMainThreadQueries()
                .setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .createFromAsset("database/test_movie_app.db")
                .build()
        movieDao = database.movieDao()
        reminderDao = database.reminderDao()
    }

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun readAndWriteMovieToDatabase() = testScope.runBlockingTest {

        val movie: Movie = sampleMovies.last()

        movieDao.insertMovie(movie)
        val movieById = movieDao.getMovieDetailsById(movieId = movie.id)

        assertEquals(movieById, movie)
    }

    @Test
    fun testPrePopulatedInput() = testScope.runBlockingTest {

        val movieById = movieDao.getMovieDetailsById(movieId = testMovie.id)
        assertEquals(movieById, testMovie)
    }

    @Test
    fun testUpdatingDb() = testScope.runBlockingTest {

        val updatedMovie = testMovie.copy(genres = testMovie.genres+"Drama")

        movieDao.getMovieDetailsById(testMovie.id)
        movieDao.updateMovie(updatedMovie)
        val movieById = movieDao.getMovieDetailsById(movieId = testMovie.id)

        assertEquals(movieById, updatedMovie)
    }

    @Test
    fun getAllReminders() = testScope.runBlockingTest {
        val reminders = reminderDao.getAllReminders()
        assert(reminders.isNotEmpty())
    }

    @Test
    fun findReminder() = testScope.runBlockingTest {

        val reminderById = reminderDao.getReminderById(testReminder.movieId)

        assertEquals(reminderById, testReminder)
    }

    @Test
    fun findAllMoviesWithReminders() = testScope.runBlockingTest {
        val moviesWithReminder = reminderDao.getMoviesWithReminders()
        moviesWithReminder.collect { assert(it.isNotEmpty()) }
    }
}