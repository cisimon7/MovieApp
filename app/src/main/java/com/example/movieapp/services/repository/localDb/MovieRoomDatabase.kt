package com.example.movieapp.services.repository.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.services.model.Converters
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.MovieImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Database(
    entities = [Movie::class, MovieImages::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun imagesDao(): MovieImagesDao

    companion object {

        var lastUpdated: Instant = Clock.System.now()

        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getDatabase(context: Context): MovieRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "main_movie_database"
                ).createFromAsset("database/movie_app.db").setQueryCallback(
                    { _, _ -> this.lastUpdated = Clock.System.now() },
                    Dispatchers.Default.asExecutor()
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}