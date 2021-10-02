package com.example.movieapp.services.repository.localDb

import android.content.Context
import androidx.room.*
import com.example.movieapp.services.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Database(
    entities = [Movie::class, Reminder::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun reminderDao(): ReminderDao

    companion object {

        var lastUpdated: Instant = Clock.System.now()

        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getDatabase(context: Context): MovieRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "movie_app_database4"
                ).createFromAsset("database/test_movie_app.db")
                .setQueryCallback(
                    { _, _ -> this.lastUpdated = Clock.System.now() },
                    Dispatchers.Default.asExecutor()
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}