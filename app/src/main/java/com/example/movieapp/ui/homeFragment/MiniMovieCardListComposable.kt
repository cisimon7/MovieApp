package com.example.movieapp.ui.homeFragment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieapp.R
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.ui.generalComponents.Loading
import com.example.movieapp.ui.generalComponents.RatingCircleItem
import com.example.movieapp.ui.colorByRating
import com.example.movieapp.ui.darken
import com.example.movieapp.ui.modifierExtensions.MiniMovieCardStructure
import com.example.movieapp.ui.modifierExtensions.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MiniMovieCardListComposable(
    pagedMovieList: StateFlow<PagingData<Movie>>,
    addMoreOnScrollEndFun: () -> Job,
    displayMovieDetails: (Int) -> Unit
) {

    val movieListItems: LazyPagingItems<Movie> = pagedMovieList.collectAsLazyPagingItems()
    var isUpdating by remember { mutableStateOf(false) }

    movieListItems.apply {
        when (loadState.append) {
            !is LoadState.Loading -> {
                isUpdating = false
            }
            is LoadState.Loading -> {
                if (!isUpdating) addMoreOnScrollEndFun()
                true.also { isUpdating = it }
            }
        }
    }

    Column {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(5.dp),
            modifier = Modifier,
            state = rememberLazyListState(),
            content = {
                if (movieListItems.itemCount != 0) {
                    items(movieListItems.itemCount) { movie_index ->
                        movieListItems[movie_index]?.let { MiniMovieCard(it, displayMovieDetails) }
                    }
                }
            }
        )
        AnimatedVisibility(visible = isUpdating, Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth()) {
                Loading(Modifier.align(Alignment.Center))
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MiniMovieCardListTopBar(isUpdating: Boolean) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .glassiness(murkiness = 0.7F, cornerRadius = 5.dp, MovieAppColorTheme.colors.primary2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Popular Movies",
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
            textAlign = TextAlign.Center,
            style = MovieAppTypography.h5,
            fontWeight = FontWeight.Bold,
            color = MovieAppColorTheme.colors.secondary2
        )
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MiniMovieCard(movieData: Movie, onClick: (Int) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    fun Modifier.borderStyle() = when (expanded) {
        true -> this.border(
            width = 5.dp,
            color = colorByRating(movieData.vote_average).darken(0.5F),
            shape = RoundedCornerShape(5.dp)
        )
        false -> this
    }

    Card(
        modifier = Modifier
            .size(120.dp, 250.dp)
            .padding(5.dp)
            .borderStyle()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { },
                    onLongPress = { expanded = !expanded },
                    onTap = { onClick(movieData.id) }
                )
            },
        shape = RoundedCornerShape(5.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {

        MiniMovieCardDropDownMenu(movieData, expanded, Modifier.width(255.dp)) { expanded = false }

        Column(
            Modifier
                .fillMaxSize()
                .glassiness(0.5F)
                .padding(3.dp)) {

            Box(Modifier.weight(0.9F)) { MiniMovieCardImage(movieData) }

            Column(
                Modifier
                    .padding(5.dp)
                    .weight(0.2F)) {
                Text(
                    text = movieData.title,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 10.sp,
                    style = MovieAppTypography.h3,
                    color = MovieAppColorTheme.colors.secondary1
                )
                Text(
                    text = movieData.release_date.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 7.sp,
                    color = MovieAppColorTheme.colors.secondary2,
                    style = MovieAppTypography.h5
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun MiniMovieCardImage(movieData: Movie) {
    MiniMovieCardStructure(Modifier.background(Color.Transparent)) {
        Image(
            painter = rememberImagePainter(
                data = movieData.cover_url,
                builder = {  placeholder(R.drawable.image_placeholder_3) }
            ),
            contentDescription = "Cover Image ${movieData.title}",
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
        RatingCircleItem(movieData.vote_average, Modifier.size(40.dp))
        Box(Modifier.background(Color.Transparent)) {}
    }
}


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


@Preview(showBackground = true, backgroundColor = 0xFF2ECC71)
@Composable
fun ShowCardPreview() {
    MiniMovieCard(sampleMovies.first()) { }
}


@Preview
@Composable
fun DropDownPreview() {
    MiniMovieCardDropDownMenu(sampleMovies.first(), true) { }
}