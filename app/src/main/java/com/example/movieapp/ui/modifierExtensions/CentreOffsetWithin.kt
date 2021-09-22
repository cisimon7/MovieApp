package com.example.movieapp.ui.modifierExtensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

fun Modifier.centreOffsetWithin(x: Float = 0.1F, y: Float = 0.1F): Modifier =
    this.then(CentreOffsetWithin(x, y))

class CentreOffsetWithin(private val x: Float, private val y: Float) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val (parentWidth, parentHeight) = with(constraints) { maxWidth to maxHeight }
        val placeable = measurable.measure(constraints)

        return layout(parentWidth, parentHeight) {
            placeable.place(
                (x * parentWidth - 0.5 * placeable.width).toInt().coerceIn(0..(parentWidth - placeable.width)),
                (y * parentHeight - 0.5 * placeable.height).toInt().coerceIn(0..(parentHeight - placeable.height))
            )
        }
    }
}