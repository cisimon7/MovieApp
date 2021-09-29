package com.example.movieapp.ui.generalComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Loading(modifier: Modifier = Modifier) {

    val angle by rememberInfiniteTransition().animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(tween(1_000, easing = LinearEasing))
    )

    val radius = 50F
    val thickness = 15F

    Box(modifier = modifier
        .size((radius + 0.6 * thickness).dp)
        .drawBehind {

            rotate(angle) {
                drawCircle(
                    Brush.sweepGradient(listOf(Color.White, Color.Blue)),
                    radius = radius,
                    center,
                    style = Stroke(thickness, cap = StrokeCap.Round)
                )
                drawCircle(
                    color = Color.Blue,
                    radius = (0.6 * thickness).toFloat(),
                    center = center + Offset(radius, 0F)
                )
            }
        })
}