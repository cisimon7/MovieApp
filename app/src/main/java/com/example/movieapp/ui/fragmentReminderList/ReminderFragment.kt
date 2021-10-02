package com.example.movieapp.ui.fragmentReminderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.movieapp.ui.extensionsModifier.dividerColor
import com.example.movieapp.viewModel.ReminderListViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.android.ext.android.inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ReminderFragment : Fragment() {

    private lateinit var controller: NavController
    private val reminderListViewModel by inject<ReminderListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            reminderListViewModel.fetchMoviesWithReminders()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        controller = this@ReminderFragment.findNavController()

        val openMovieDetails = { movieId: Int ->
            val action = ReminderFragmentDirections.actionReminderFragmentToDetailedCardFragment(movieId)
            controller.navigate(action)
        }

        val tz = TimeZone.currentSystemDefault()

        setContent {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val groupedReminderList = reminderListViewModel.groupedReminderList.collectAsState()

                LazyColumn(Modifier.fillMaxSize()) {
                    groupedReminderList.value?.forEach { (group, movieAndReminders) ->
                        stickyHeader {
                            ReminderListStickyHeader(group)
                        }
                        items(movieAndReminders) { (reminder, movieData) ->
                            MovieReminderItem(dividerColor, movieData, tz, reminder.dateTime, openMovieDetails)
                        }
                    }
                }
            }
        }
    }
}