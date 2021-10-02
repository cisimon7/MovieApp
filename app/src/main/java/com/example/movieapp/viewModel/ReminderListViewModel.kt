package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movieapp.services.model.MovieWithReminder
import com.example.movieapp.services.model.Reminder
import com.example.movieapp.services.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class ReminderListViewModel(private val repository: MovieRepository): ViewModel() {

    private val tz = TimeZone.currentSystemDefault()

    private var _reminderList: MutableStateFlow<List<MovieWithReminder>> = MutableStateFlow(emptyList())

    var reminderList: MutableStateFlow<List<MovieWithReminder>> = _reminderList

    val groupedReminderList: StateFlow<SortedMap<DurationGrouping, List<MovieWithReminder>>?> =
        _reminderList.transformLatest { reminders ->
            emit(
                reminders.groupBy {
                    val duration = (Clock.System.now().toLocalDateTime(tz) - it.reminder.dateTime).absoluteValue
                    when {
                        duration < Duration.days(1)  -> DurationGrouping.Today
                        duration < Duration.days(7)  -> DurationGrouping.ThisWeek
                        duration < Duration.days(30) -> DurationGrouping.ThisMonth
                        else                               -> DurationGrouping.Later
                    }
                }.toSortedMap { p0, p1 ->
                    when {
                        p0.order == p1.order -> 0
                        p0.order > p1.order -> 1
                        else -> -1
                    }
                }
            )
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = null)


    suspend fun fetchMoviesWithReminders() {
        repository.getMoviesWithReminders()
            .collectLatest { reminders ->
                _reminderList.value=reminders
            }
    }

    suspend fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
        }
    }

    suspend fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    suspend fun deleteMovieWithReminderById(movieId: Int) {
        viewModelScope.launch {
            repository.deleteReminderById(movieId)
        }
    }
}

sealed class DurationGrouping(val value: String, val order: Int) {
    object Today : DurationGrouping("Today",1)
    object ThisWeek : DurationGrouping("This Week",2)
    object ThisMonth : DurationGrouping("This Month",3)
    object Later : DurationGrouping("Later this year",4)
}

@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.minus(other: LocalDateTime): Duration {
    val tz = TimeZone.currentSystemDefault()
    return this.toInstant(tz) - other.toInstant(tz)
}