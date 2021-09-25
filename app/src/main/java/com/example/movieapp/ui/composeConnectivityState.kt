package com.example.movieapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay

@OptIn(InternalCoroutinesApi::class, ExperimentalAnimationApi::class)
@Composable
fun ConnectivityComposable(connectivityState: ConnectionState) {

    when (connectivityState) {
        ConnectionState.Available -> {
            var visibility by remember { mutableStateOf(true) }

            LaunchedEffect(key1 = Unit) {
                delay(1_000)
                visibility = false
            }

            AnimatedVisibility(visible = visibility) {
                ConnectivityOnline()
            }
        }
        ConnectionState.Unavailable -> ConnectivityOffline()
    }
}

@Composable
fun PresentConnectivityStatusComposable(text: String, backgroundColor: Color, statusImage: Int) {
    Box(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth().background(backgroundColor, shape = RoundedCornerShape(7.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = statusImage),
                contentDescription = "Network Status",
                modifier = Modifier.padding(10.dp, 3.dp).size(30.dp)
            )
            Text(text = text, color = Color.White, style = MovieAppTypography.body1, fontSize = 20.sp)
        }
    }
}


@Preview
@Composable
fun ConnectivityOnline() {
    PresentConnectivityStatusComposable(
        "Online",
        Color(0xFF2ECC71),
        R.drawable.icons_cloud_checked_48px
    )
}

@Preview
@Composable
fun ConnectivityOffline() {
    PresentConnectivityStatusComposable(
        "Offline",
        Color(0xFFEC7063),
        R.drawable.icons_cloud_cross_48px
    )
}

