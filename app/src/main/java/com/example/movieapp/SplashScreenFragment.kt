package com.example.movieapp

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.delay

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            SplashScreenAnimation()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun SplashScreenAnimation() {

    val scale by remember { mutableStateOf(Animatable(0F, Float.VectorConverter)) }
    val angle by remember { mutableStateOf(Animatable(0F, Float.VectorConverter)) }

    LaunchedEffect(key1 = Unit) {
        delay(500)
        scale.animateTo(
            1F,
            spring(Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium)
        )
        angle.animateTo(
            360F,
            infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart)
        )
    }

    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7F))) {

        val annotatedTitle = AnnotatedString.Builder("TMDB").apply {
            addStyle(SpanStyle(color = Color.Blue),0,1)
            addStyle(SpanStyle(color = Color.Green),1,2)
            addStyle(SpanStyle(color = Color.Yellow),2,3)
            addStyle(SpanStyle(color = Color.Red),3,4)
        }

        Text(
            text = annotatedTitle.toAnnotatedString(),
            fontWeight = FontWeight.Bold,
            style = MovieAppTypography.h4,
            modifier = Modifier.align(Alignment.Center).scale(scale.value)
        )

        Canvas(Modifier.align(Alignment.Center)) {
            val radius = 150F
            val thickness = 20F

            val step_value = 18
            val p0 = center + Offset(0F, radius)
            val p1 = center + Offset(0F, radius + 50F)

            (0 until 360 step step_value).forEach { deg ->
                rotate(angle.value + deg.toFloat(), center) {
                    drawLine(
                        color = Color(0xFFFFA07A).copy(alpha = 0.5F),
                        start = -p0,
                        end = -p1,
                        strokeWidth = thickness,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}