package com.example.movieapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.fancyColors

@Composable
fun MovieAppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    drawerContent: @Composable (ColumnScope.() -> Unit),
    content: @Composable (PaddingValues) -> Unit,
) {

    MovieAppTheme {
        Scaffold(
            modifier = Modifier.background(
                brush = MovieAppTheme.colors.overlayBrush
            ),
            scaffoldState = scaffoldState,
            topBar = topBar,
            drawerContent = drawerContent,
            drawerBackgroundColor = Color.White,
            /*drawerContentColor = MovieAppTheme.colors.background,*/
            drawerScrimColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            /*contentColor = MovieAppTheme.colors.background,*/
            content = content
        )
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