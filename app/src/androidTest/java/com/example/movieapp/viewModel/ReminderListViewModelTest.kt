package com.example.movieapp.viewModel

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.movieapp.services.model.MovieWithReminder
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope

import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderListViewModelTest : KoinTest {

    private lateinit var viewModel: ReminderListViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @OptIn(InternalAPI::class)
    @Before
    fun loadKoinModulesAndViewModel() {

        stopKoin()
        val context: Context = ApplicationProvider.getApplicationContext()
        val database: MovieRoomDatabase =
            Room.inMemoryDatabaseBuilder(context, MovieRoomDatabase::class.java)
                .setTransactionExecutor(testDispatcher.asExecutor())
                .setQueryExecutor(testDispatcher.asExecutor())
                .build()

        startKoin { testModules(database) }
        viewModel = get()
    }

    @Test
    fun getReminders() = runBlocking {
        viewModel.fetchMoviesWithReminders()
        viewModel.reminderList.value.forEach {
            Log.i(
                "CISIMON7",
                json.encodeToString(MovieWithReminder.serializer(), it)
            )
        }
    }
}