package com.example.movieapp.ui.modifierExtensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
fun MiniCardLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {

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

