package com.example.movieapp.services.repository.localDb

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
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
    private lateinit var movieImagesDao: MovieImagesDao

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val json = Json { prettyPrint = true }

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.databaseBuilder(context, MovieRoomDatabase::class.java, "test_database")
                .setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .createFromAsset("database/test_movie_app.db")
                .build()
        movieDao = database.movieDao()
        movieImagesDao = database.imagesDao()
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

        /*input sample movie is same as sampleMovies[1]*/

        val movie: Movie = sampleMovies[1]
        val updatedMovie = movie.copy(genres = movie.genres+"Dramaaaa")

        movieDao.getMovieDetailsById(movie.id)
        movieDao.updateMovie(updatedMovie)
        val movieById = movieDao.getMovieDetailsById(movieId = movie.id)

//        assertEquals(movieById, movie)
        assertEquals(movieById, updatedMovie)
    }
}