package com.example.movieapp.ui.scaffoldComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.ui.modifierExtensions.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography

@Composable
fun MovieAppBar(onNavIconPressed: () -> Unit = { }, title: String) {
    Column(Modifier) {
        TopAppBar(
            modifier = Modifier .glassiness(murkiness = 0.7F, glassColor=MovieAppColorTheme.colors.primary2),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = MaterialTheme.colors.onSurface,
            actions = {
                Image(
                    painter = painterResource(id = R.drawable.search_left_black),
                    contentDescription = "Search button",
                    modifier = Modifier.padding(horizontal = 10.dp).size(20.dp)
                )
            },
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    style = MovieAppTypography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MovieAppColorTheme.colors.secondary2
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.menu_black),
                    contentDescription = "Navigate back stack",
                    modifier = Modifier
                        .clickable(onClick = onNavIconPressed)
                        .padding(horizontal = 16.dp).size(20.dp)
                )
            }
        )
        Divider()
    }
}