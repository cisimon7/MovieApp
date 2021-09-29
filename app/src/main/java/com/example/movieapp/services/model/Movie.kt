package com.example.movieapp.services.model

import androidx.room.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "movie_table",
    primaryKeys = ["id", "cover_url", "backdrop_url"]
)
@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val cover_url: String,
    val tagline: String,
    val overview: String,
    var genres: List<String> = emptyList(),
    val release_date: LocalDate,
    val vote_average: Float,
    val vote_count: Int,
    val backdrop_url: String,
    val runtime: Int,
    val revenue: Int,
    val budget: Int,
    val adult: Boolean,
    val status: Status
) {
    fun update(other: Movie) {
        this.genres = other.genres
    }
}

@Entity(
    tableName = "images_table",
    primaryKeys = ["cover_url", "backdrop_url"]
)
data class MovieImages(
    val cover_url: String,
    val backdrop_url: String,
    val cover_bitmap: String,
    val backdrop_bitmap: String
)

data class MovieAndImages(
    @Embedded val movie: Movie,
    @Relation(parentColumn = "cover_url", entityColumn = "cover_url") val movieImages: MovieImages
)


/*
* I could use just movie id and reminder date to create a new table to save space and avoid
* duplication*/
data class MovieWithReminder(
    val movie: Movie,
    val reminderDate: LocalDateTime
)