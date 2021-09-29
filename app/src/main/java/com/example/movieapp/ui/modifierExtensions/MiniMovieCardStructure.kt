package com.example.movieapp.ui.modifierExtensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.generalComponents.RatingCircleItem

@Composable
fun MiniMovieCardStructure(modifier: Modifier = Modifier, content: @Composable () -> Unit) {

    Layout(content = content, modifier = modifier) { measurables, constraints ->

        val (parentWidth, parentHeight) = with(constraints) { maxWidth to maxHeight }
        val ratingPlaceable = measurables[1].measure(constraints)
        val imgPlaceable = measurables[0].measure(
            Constraints.fixed(
                parentWidth,
                (parentHeight - (0.5 * ratingPlaceable.height)).toInt()
            )
        )
        val optionsPlaceable = measurables[2].measure(constraints)

        layout(parentWidth, parentHeight) {
            imgPlaceable.place(0, 0)
            ratingPlaceable.place(
                (0.25 * ratingPlaceable.width).toInt(),
                imgPlaceable.height - (0.5 * ratingPlaceable.height).toInt()
            )
            optionsPlaceable.place(
                imgPlaceable.width - (1.25 * optionsPlaceable.width).toInt(),
                (0.25 * optionsPlaceable.width).toInt()
            )
        }
    }
}

@Preview
@Composable
fun ShowMiniCardLayout() {
    Card(Modifier, backgroundColor = Color.Transparent) {
        MiniMovieCardStructure(Modifier) {
            Box(
                Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.5F))) { }
            RatingCircleItem(78F, Modifier.size(200.dp))
            Box(
                Modifier
                    .size(100.dp)
                    .background(Color.Red)) { }
        }
    }
}

