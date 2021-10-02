package com.example.movieapp.services.repository.localDb

import androidx.room.*
import com.example.movieapp.services.model.MovieWithReminder
import com.example.movieapp.services.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder_table")
    suspend fun getAllReminders(): List<Reminder>

    @Query("SELECT * FROM reminder_table WHERE movieId =:movieId LIMIT 1")
    suspend fun getReminderById(movieId: Int): Reminder

    @Insert
    suspend fun addReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder:Reminder)

    @Query("DELETE FROM reminder_table WHERE movieId =:movieId")
    suspend fun deleteReminderById(movieId: Int)

    @Transaction
    @Query("SELECT * From reminder_table")
    fun getMoviesWithReminders(): Flow<List<MovieWithReminder>>
}