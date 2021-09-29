package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.rememberImagePainter
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.MovieWithReminder
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.ui.generalComponents.RatingCircleItem
import com.example.movieapp.ui.modifierExtensions.MiniMovieCardStructure
import com.example.movieapp.ui.modifierExtensions.dividerColor
import com.example.movieapp.ui.modifierExtensions.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.datetime.*
import kotlin.math.abs
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ReminderFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {

        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now()

        setContent {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val movieAndReminders = sampleMovies.map { movie ->
                    MovieWithReminder(
                        movie,
                        Instant.fromEpochMilliseconds(
                            Random.nextLong(
                                LocalDateTime(2021, 9, 27, 0, 0, 0, 0).toInstant(tz)
                                    .toEpochMilliseconds(),
                                LocalDateTime(2021, 10, 30, 0, 0, 0, 0).toInstant(tz)
                                    .toEpochMilliseconds()
                            )
                        ).toLocalDateTime(tz)
                    )
                }

                val movieAndRemindersGrouped = movieAndReminders.groupBy {
                    val duration = (Clock.System.now().toLocalDateTime(tz) - it.reminderDate).absoluteValue
                    when {
                        duration < Duration.days(1) -> DurationGrouping.Today
                        duration < Duration.days(7) -> DurationGrouping.ThisWeek
                        duration < Duration.days(30) -> DurationGrouping.ThisMonth
                        else -> DurationGrouping.Later
                    }
                }.toSortedMap { p0, p1 ->
                    when {
                        p0.order == p1.order -> 0
                        p0.order > p1.order -> 1
                        else -> -1
                    }
                }

                LazyColumn(Modifier.fillMaxSize()) {
                    movieAndRemindersGrouped.forEach { (group, movieAndReminders) ->
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxSize()
                                    .background(
                                        MovieAppColorTheme.colors.ascent1,
                                        RoundedCornerShape(5.dp)
                                    )
                                    .border(
                                        3.dp,
                                        color = MovieAppColorTheme.colors.secondary2,
                                        RoundedCornerShape(5.dp)
                                    )
                            ) {
                                Text(
                                    text = group.value,
                                    style = MovieAppTypography.h5,
                                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                    color = MovieAppColorTheme.colors.primary1
                                )
                            }
                        }
                        items(movieAndReminders) { (movieData, date) ->
                            MovieReminderItem(dividerColor, movieData, tz, date)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun MovieReminderItem(
    dividerColor: Color,
    movieData: Movie,
    tz: TimeZone,
    date: LocalDateTime
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .glassiness(0.5F, 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .weight(0.25F)
                .padding(10.dp)
                .background(Color.Transparent)
                .size(100.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.icons_circled_male),
                contentDescription = "Cover Image ${movieData.title}",
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(3.dp)).background(Color.Transparent),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.alarm_colored),
                contentDescription = "Alarm image",
                modifier = Modifier.size(25.dp).align(Alignment.TopEnd),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.weight(0.55F).padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = movieData.title,
                modifier = Modifier.fillMaxWidth(),
                style = MovieAppTypography.h6,
                color = MovieAppColorTheme.colors.secondary1
            )
            Text(
                text = movieData.genres.joinToString(", "),
                modifier = Modifier.fillMaxWidth(),
                style = MovieAppTypography.body1,
                color = MovieAppColorTheme.colors.secondary2
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight().width(3.dp).background(MovieAppColorTheme.colors.ascent2))

        Column(Modifier.weight(0.2F)) {
            val timeNow = Clock.System.now().toLocalDateTime(tz)
            val timeLeft: Duration = date - timeNow

            Text(text = timeLeftFormatter(timeLeft), color = MovieAppColorTheme.colors.secondary1)
        }
    }
    Divider(
        modifier = Modifier.padding(horizontal = 10.dp),
        color = dividerColor
    )
}


@OptIn(ExperimentalTime::class)
fun timeLeftFormatter(timeSpan: Duration): String {
    val duration = timeSpan.absoluteValue
    return when {
        duration.inWholeSeconds <= 60 -> "${duration.inWholeSeconds} Second(s) Left"
        duration.inWholeMinutes <= 60 -> "${duration.inWholeMinutes} Minute(s) Left"
        duration.inWholeHours <= 24   -> "${duration.inWholeHours} Hour(s) Left"
        else                          -> "${duration.inWholeDays} Day(s) Left"
    }
}


@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.minus(other: LocalDateTime): Duration {
    val tz = TimeZone.currentSystemDefault()
    return this.toInstant(tz) - other.toInstant(tz)
}

sealed class DurationGrouping(val value: String, val order: Int) {
    object Today : DurationGrouping("Today",1)
    object ThisWeek : DurationGrouping("This Week",2)
    object ThisMonth : DurationGrouping("This Month",3)
    object Later : DurationGrouping("Later this year",4)
}