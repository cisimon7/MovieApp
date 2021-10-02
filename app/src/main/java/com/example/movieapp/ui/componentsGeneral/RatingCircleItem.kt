package com.example.movieapp.ui.componentsGeneral

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.ui.colorByRating
import com.example.movieapp.ui.componentsScaffold.FilterItem
import com.example.movieapp.ui.extensionsModifier.centreOffsetWithin
import com.example.movieapp.ui.theme.MovieAppTypography

@Composable
fun RatingCircleItem(percentage: Float, modifier: Modifier = Modifier) {

    Box(modifier.size(50.dp).background(Color.Transparent).drawWithContent {

            val radius = center.x
            val thickness = 0.2F * radius
            val padding = 1 * thickness
            val angle = 360 * (percentage / 100)
            val color = colorByRating(percentage)

            drawCircle(
                color = Color.Black,
                radius = radius + thickness - padding,
                style = Fill
            )
            drawCircle(
                color.copy(alpha = 0.4F),
                radius = radius - padding,
                style = Stroke(width = thickness)
            )
            rotate(-90F) {
                drawPath(
                    path = Path().apply {
                        arcTo(Rect(center, radius - padding), 0F, angle, false)
                    },
                    color = color,
                    style = Stroke(width = thickness)
                )
            }
            drawContent()
        },
        content = {
            Row(Modifier.centreOffsetWithin(0.5F, 0.5F), verticalAlignment = Alignment.Top) {
                Text(
                    text = "${percentage.toInt()}",
                    style = MovieAppTypography.h6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier
                )
                Text(
                    text = "%",
                    style = MovieAppTypography.h6,
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier
                )
            }
        }
    )
}

@Preview
@Composable
fun RatingCircleItemPreview() {
    Column(
        Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RatingCircleItem(32F)
        RatingCircleItem(62F)
        RatingCircleItem(82F)
    }
}