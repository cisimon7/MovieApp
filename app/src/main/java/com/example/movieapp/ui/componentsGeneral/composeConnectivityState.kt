package com.example.movieapp.ui.componentsGeneral

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

    var visibility by remember { mutableStateOf(true) }

    val hide = { visibility = false }

    AnimatedVisibility(visible = visibility) {
        when (connectivityState) {
            ConnectionState.Available -> {
                LaunchedEffect(key1 = Unit) {
                    delay(1_000)
                    visibility = false
                }
                ConnectivityOnline(hide)
            }
            ConnectionState.Unavailable -> ConnectivityOffline(hide)
        }
    }
}

@Composable
fun PresentConnectivityStatusComposable(
    hide: () -> Unit,
    text: String,
    backgroundColor: Color,
    statusImage: Int
) {
    Box(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .background(backgroundColor, shape = RoundedCornerShape(7.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = statusImage),
                contentDescription = "Network Status",
                modifier = Modifier
                    .padding(10.dp, 3.dp)
                    .size(25.dp)
            )
            Text(
                text = text,
                color = Color.White,
                style = MovieAppTypography.body1,
                fontSize = 15.sp
            )
        }
        Image(
            painter = painterResource(id = R.drawable.cancel_black),
            contentDescription = "Network Status",
            modifier = Modifier
                .padding(20.dp, 3.dp)
                .size(20.dp)
                .clickable(onClickLabel = "Hide") { hide() }
                .align(Alignment.CenterEnd)
        )
    }
}


@Composable
fun ConnectivityOnline(hide: () -> Unit) {
    PresentConnectivityStatusComposable(
        hide,
        "Online",
        Color(0xFF2ECC71),
        R.drawable.icons_cloud_checked_48px
    )
}

@Composable
fun ConnectivityOffline(hide: () -> Unit) {
    PresentConnectivityStatusComposable(
        hide,
        "Offline",
        Color(0xFFEC7063),
        R.drawable.icons_cloud_cross_48px
    )
}

@Preview
@Composable
fun OnlinePreview() {
    ConnectivityOnline { }
}

@Preview
@Composable
fun OfflinePreview() {
    ConnectivityOffline { }
}

