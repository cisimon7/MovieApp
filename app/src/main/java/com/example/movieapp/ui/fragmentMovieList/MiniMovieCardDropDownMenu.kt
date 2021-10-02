package com.example.movieapp.ui.fragmentMovieList

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.ui.extensionsModifier.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography

@Composable
fun MiniMovieCardDropDownMenu(
    movie: Movie,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded,
        onDismiss,
        modifier.glassiness(murkiness = 0.15F, glassColor = MovieAppColorTheme.colors.ascent1)
    ) {
        Text(
            text = "Movie Title: ${movie.title}",
            Modifier.padding(10.dp),
            style = MovieAppTypography.h6)
        DropdownMenuItem(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(5.dp)
                .glassiness(0.8F, 5.dp, glassColor = MovieAppColorTheme.colors.primary1)
        ) {
            Text(
                "Add to favorite list",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                style = MovieAppTypography.body1,
                color = MovieAppColorTheme.colors.secondary1)
        }
        DropdownMenuItem(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(5.dp)
                .glassiness(0.8F, 5.dp, glassColor = MovieAppColorTheme.colors.primary1)
        ) {
            Text(
                "Set reminder for movie",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                style = MovieAppTypography.body1,
                color = MovieAppColorTheme.colors.secondary1)
        }
        DropdownMenuItem(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(5.dp)
                .glassiness(0.8F, 5.dp, glassColor = MovieAppColorTheme.colors.primary1)
        ) {
            Text(
                "Show movie details",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                style = MovieAppTypography.body1,
                color = MovieAppColorTheme.colors.secondary1)
        }
        DropdownMenuItem(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(5.dp)
                .glassiness(0.8F, 5.dp, glassColor = MovieAppColorTheme.colors.primary1)
        ) {
            Text(
                "View your rating on movie",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                style = MovieAppTypography.body1,
                color = MovieAppColorTheme.colors.secondary1)
        }
    }
}


@Preview
@Composable
fun DropDownPreview() {
    MiniMovieCardDropDownMenu(sampleMovies.first(), true) { }
}