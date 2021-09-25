package com.example.movieapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class MovieAppColors(
    val glassColor: Color,
    val primary1: Color,
    val primary2: Color,
    val secondary1: Color,
    val secondary2: Color,
    val ascent1: Color,
    val ascent2: Color
) {
    /*listOf(Color(0xFF7AE2FF), Color(0xFFFFA07A), Color(0xFF2ECC71))*/
    val overlayBrush = Brush.linearGradient(
        listOf(primary1, primary2)
    )
}

val colorPalette1 = MovieAppColors(
    Color.White,
    Color(0xFFf1dca7),
    Color(0xFFf2e9e4),
    Color(0xFF4a4e69),
    Color(0xFF0e606b),
    Color(0xFF124ceb),
    Color(0xFF822CDE)
)

val colorPalette2 = MovieAppColors(
    Color.Black,
    Color(0xFF312f30),
    Color(0xFF435058),
    Color(0xFFf8f7ff),
    Color(0xFFffcad4),
    Color(0xFFf0f3bd),
    Color(0xFFffffff),
)

object MovieAppTheme {
    val colors: MovieAppColors
        @Composable get() = LocalMovieAppColors.current
}

@Composable
fun ProvideMovieAppColors(colors: MovieAppColors, content: @Composable () -> Unit) {
    val colorPalette = remember { colors.copy() }
    CompositionLocalProvider(LocalMovieAppColors provides colorPalette, content = content)
}

private val LocalMovieAppColors = staticCompositionLocalOf<MovieAppColors> {
    error("No MovieAppColorPalette provided")
}