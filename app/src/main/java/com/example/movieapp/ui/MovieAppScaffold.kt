package com.example.movieapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.fancyColors
import kotlinx.coroutines.launch

@Composable
fun MovieAppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navToHome: () -> Unit,
    navToProfile: () -> Unit,
    navToMovieList: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {

    val scope = rememberCoroutineScope()

    val onNavIconPressed: () -> Unit = { scope.launch { scaffoldState.drawerState.open() } }

    MovieAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { MovieAppBar(onNavIconPressed) },
                drawerContent = { MovieAppDrawer(navToHome, navToProfile, navToMovieList) },
                content = content
            )
        }
    }
}


@Composable
fun SampleMainScreen() {
    MovieAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(10) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                fancyColors.random(), shape = RoundedCornerShape(5.dp)
                            )
                    )
                }
            }
        }
    }
}