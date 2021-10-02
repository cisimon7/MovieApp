package com.example.movieapp.ui.fragmentReminderList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import com.example.movieapp.viewModel.DurationGrouping


@Composable
fun ReminderListStickyHeader(group: DurationGrouping) {
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
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            color = MovieAppColorTheme.colors.primary1
        )
    }
}