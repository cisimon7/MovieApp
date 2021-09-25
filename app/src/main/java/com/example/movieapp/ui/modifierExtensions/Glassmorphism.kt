package com.example.movieapp.ui.modifierExtensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.MovieAppTheme

@Composable
fun GlassyBox(
    modifier: Modifier = Modifier,
    murkiness: Float = 0.0F,
    cornerRadius: Dp = 0.dp,
    color: Color = MovieAppTheme.colors.glassColor,
    content: @Composable BoxScope.() -> Unit
) {

    val transparency = when (color) {
        Color.Black -> 1-murkiness
        else -> murkiness
    }

    Box(
        modifier = modifier
            .background(
                color.copy(alpha = transparency),
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}

@Composable
fun Modifier.glassiness(murkiness: Float, cornerRadius: Dp = 0.dp): Modifier {
    val transparency = when (MovieAppTheme.colors.glassColor) {
        Color.Black -> 1-murkiness
        else -> murkiness
    }
    return this.background(
        Color.Black.copy(alpha = transparency),
        shape = RoundedCornerShape(cornerRadius)
    )
}

val dividerColor: Color
    @Composable get() = when (MovieAppTheme.colors.glassColor) {
    Color.Black -> Color.White.copy(alpha = 0.3f)
    else -> Color.Black.copy(alpha = 0.3f)
}
