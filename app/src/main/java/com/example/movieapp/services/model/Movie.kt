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

@Entity(tableName = "reminder_table")
@Serializable
data class Reminder(
    @PrimaryKey val movieId: Int,
    val dateTime: LocalDateTime
)


@Serializable
data class MovieWithReminder(
    @Embedded val reminder: Reminder,
    @Relation(
        parentColumn = "movieId",
        entity = Movie::class,
        entityColumn = "id"
    )
    val movie: Movie
)