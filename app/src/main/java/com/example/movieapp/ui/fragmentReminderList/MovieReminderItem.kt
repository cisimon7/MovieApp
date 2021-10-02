package com.example.movieapp.ui.fragmentReminderList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.services.model.Movie
import com.example.movieapp.ui.componentsGeneral.ImageLoaderComposable
import com.example.movieapp.ui.extensionsModifier.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import com.example.movieapp.viewModel.minus
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun MovieReminderItem(
    dividerColor: Color,
    movieData: Movie,
    tz: TimeZone,
    date: LocalDateTime,
    openMovieDetails: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { openMovieDetails(movieData.id) }
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

            ImageLoaderComposable(
                modifier = Modifier,
                imageUrl = movieData.cover_url,
                imageLabel = movieData.title
            )
            Image(
                painter = painterResource(id = R.drawable.alarm_colored),
                contentDescription = "Alarm image",
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .weight(0.55F)
                .padding(10.dp),
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

        Box(modifier = Modifier.weight(0.05F)
            .padding(vertical = 5.dp)
            .fillMaxHeight()
            .width(5.dp)
            .background(MovieAppColorTheme.colors.ascent2))

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
        duration.inWholeDays <= 30    -> "${duration.inWholeDays} Day(s) Left"
        else                          -> "${duration.inWholeDays/30} Month(s) Left"
    }
}