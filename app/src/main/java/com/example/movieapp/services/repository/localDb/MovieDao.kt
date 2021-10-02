package com.example.movieapp.services.repository.localDb

import androidx.paging.PagingSource
import androidx.room.*
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.MovieWithReminder
import com.example.movieapp.services.repository.QueryStringDSL
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Update
    suspend fun updateMovieList(movies: List<Movie>)

    @Delete
    suspend fun deleteMovieById(movie: Movie)

    @Query("DELETE FROM movie_table")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM movie_table WHERE id=:movieId LIMIT 1")
    suspend fun getMovieDetailsById(movieId: Int): Movie

    @Query("SELECT * FROM movie_table WHERE vote_average >= :rating")
    fun getMoviesByRating(rating: Float): PagingSource<Int, Movie>

    @Query("SELECT * FROM movie_table")
    fun discoverMovies(): PagingSource<Int, Movie>
}