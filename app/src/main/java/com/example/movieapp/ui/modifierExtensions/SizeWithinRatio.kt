package com.example.movieapp.ui.modifierExtensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

fun Modifier.sizeWithinRatio(x: Float = 1.0F, y: Float = 1.0F): Modifier =
    this.then(SizeWithinRatio(x, y))

class SizeWithinRatio(private val x: Float, private val y: Float) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val (parentWidth, parentHeight) = with(constraints) { maxWidth to maxHeight }
        val placeable = measurable.measure(
            Constraints.fixed(
                (parentWidth * x).toInt(),
                (parentHeight * y).toInt()
            )
        )

        return layout(parentWidth, parentHeight) {
            placeable.place(0, 0)
        }
    }
}