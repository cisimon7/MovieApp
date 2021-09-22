package com.example.movieapp.services.repository.localDb

import androidx.room.*
import com.example.movieapp.services.model.MovieImages
import com.example.movieapp.services.model.Movie

@Dao
interface MovieImagesDao {

    @Insert
    suspend fun insertMovieImages(movieImages: MovieImages)

    @Update
    suspend fun updateMovieImages(movieImages: MovieImages)

    @Delete
    suspend fun deleteMovieById(movieImages: MovieImages)

    @Query("DELETE FROM images_table")
    suspend fun deleteAllMovieImages()

    @Query("SELECT * FROM images_table WHERE cover_url=:url OR backdrop_url=:url LIMIT 1")
    suspend fun getMovieImagesByUrl(url: String): MovieImages

}