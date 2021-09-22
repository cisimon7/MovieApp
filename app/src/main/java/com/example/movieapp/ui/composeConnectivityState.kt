package com.example.movieapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.services.currentConnectivityState
import com.example.movieapp.services.observeConnectivityAsFlow
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@OptIn(InternalCoroutinesApi::class, ExperimentalAnimationApi::class)
@Composable
fun ConnectivityComposable(connectivityState: ConnectionState) {
    /*val context = LocalContext.current

    val connectivityState: State<ConnectionState> =
        produceState(initialValue = context.currentConnectivityState) {
            context.observeConnectivityAsFlow().collect { this.value = it }
        }*/

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

@Preview
@Composable
fun ConnectivityOnline() {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(Color(0xFF2ECC71), shape = RoundedCornerShape(3.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icons_cloud_checked_48px),
            contentDescription = "Network Status",
            modifier = Modifier.padding(10.dp, 3.dp)
        )
        Text(text = "Online", color = Color.White, style = MovieAppTypography.body1)
    }
}

@Preview
@Composable
fun ConnectivityOffline() {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(Color(0xFFEC7063), shape = RoundedCornerShape(3.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icons_cloud_cross_48px),
            contentDescription = "Network Status",
            modifier = Modifier.padding(10.dp, 3.dp)
        )
        Text(text = "No Internet Connection", color = Color.White, style = MovieAppTypography.body1)
    }
}

