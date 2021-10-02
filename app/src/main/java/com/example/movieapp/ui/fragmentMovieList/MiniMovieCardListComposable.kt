package com.example.movieapp.ui.fragmentMovieList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.example.movieapp.services.model.Movie
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.ui.colorByRating
import com.example.movieapp.ui.componentsGeneral.ImageLoaderComposable
import com.example.movieapp.ui.componentsGeneral.Loading
import com.example.movieapp.ui.componentsGeneral.RatingCircleItem
import com.example.movieapp.ui.darken
import com.example.movieapp.ui.extensionsModifier.MiniMovieCardStructure
import com.example.movieapp.ui.extensionsModifier.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.Job
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
        if (isUpdating) {
            Box(Modifier.fillMaxWidth()) {
                Loading(Modifier.align(Alignment.Center))
            }
        }
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
        ImageLoaderComposable(
            modifier = Modifier.clip(RoundedCornerShape(3.dp)),
            imageUrl = movieData.cover_url,
            imageLabel = movieData.title
        )
        RatingCircleItem(movieData.vote_average, Modifier.size(40.dp))
        Box(Modifier.background(Color.Transparent)) {}
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF2ECC71)
@Composable
fun ShowCardPreview() {
    MiniMovieCard(sampleMovies.first()) { }
}