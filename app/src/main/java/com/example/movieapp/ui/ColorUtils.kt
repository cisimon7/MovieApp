package com.example.movieapp.ui

import androidx.compose.ui.graphics.Color

fun colorByRating(percentage: Float) = when {
    percentage <= 33 -> Color.Red
    percentage > 33 && percentage <= 66 -> Color.Yellow
    else -> Color.Green
}

fun Color.darken(ratio: Float): Color {
    return this.copy(
        red = red * (1 - ratio),
        green = green * (1 - ratio),
        blue = blue * (1 - ratio)
    )
}