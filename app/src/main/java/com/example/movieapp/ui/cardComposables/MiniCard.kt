package com.example.movieapp.ui.cardComposables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
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
import com.example.movieapp.ui.modifierExtensions.GlassyBox
import com.example.movieapp.ui.modifierExtensions.MiniCardLayout
import com.example.movieapp.ui.theme.MovieAppColors
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun ListOfMiniCards(
    pagedMovieList: StateFlow<PagingData<Movie>>,
    addMoreOnScrollEndFun: () -> Job,
    displayMovieDetails: (Int) -> Unit
) {

    val movieListItems: LazyPagingItems<Movie> = pagedMovieList.collectAsLazyPagingItems()

    val scrollState: LazyListState = rememberLazyListState()

    movieListItems.apply {
        var isUpdating by remember { mutableStateOf(false) }
        when (loadState.append) {
            !is LoadState.Loading -> {
                isUpdating = false
            }
            is LoadState.Loading -> {
                if (!isUpdating) addMoreOnScrollEndFun()
                true.also { isUpdating = it }
                Text(text = "Loading", style = MovieAppTypography.h6)
            }
        }
    }

    LazyVerticalGrid(
        cells = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier,
        state = scrollState,
        content = {
            when {
                movieListItems.itemCount != 0 -> {
                    items(movieListItems.itemCount) { movie_index ->
                        movieListItems[movie_index]
                            ?.let {
                                ShowCard(it, displayMovieDetails)
                            }
                    }
                }
                else -> {

                }
            }
        }
    )
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ShowCard(movieData: Movie, onClick: (Int) -> Unit) {

    var isSelected by remember { mutableStateOf(false) }

    fun Modifier.borderStyle() = when (isSelected) {
        true -> this.border(
            width = 2.dp,
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
                    onLongPress = { isSelected = !isSelected },
                    onTap = { onClick(movieData.id) }
                )
            },
        shape = RoundedCornerShape(5.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        content = {
            GlassyBox(modifier = Modifier.fillMaxSize(), murkiness = 0.5F) {
                Column(Modifier.fillMaxSize().padding(3.dp)) {

                    Box(modifier = Modifier.weight(0.9F)) {
                        MiniCardLayout(Modifier.background(Color.Transparent)) {
                            Image(
                                painter = rememberImagePainter(
                                    data = movieData.cover_url,
                                    builder = {
                                        placeholder(R.drawable.image_placeholder_1)
                                    }
                                ),
                                contentDescription = "Cover Image ${movieData.title}",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color.Transparent),
                                contentScale = ContentScale.Crop
                            )
                            RatingCircleItem(movieData.vote_average, Modifier.size(40.dp))
                            /*
                            * more options:
                            * Add to List (List)
                            * Favorite (Love)
                            * Watchlist (Bookmark)
                            * Your rating (Star)*/
                            Box(Modifier.background(Color.Transparent)) {}
                        }
                    }

                    Column(
                        Modifier
                            .padding(5.dp)
                            .weight(0.2F)
                    ) {
                        Text(
                            text = movieData.title,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 10.sp,
                            style = MovieAppTypography.h3,
                            color = MovieAppTheme.colors.secondary1
                        )
                        Text(
                            text = movieData.release_date.toString(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 7.sp,
                            color = MovieAppTheme.colors.secondary2,
                            style = MovieAppTypography.h5
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF2ECC71)
@Composable
fun ShowCardPreview() {
    GlassyBox(modifier = Modifier, murkiness = 0.75F, cornerRadius = 5.dp) {
        ShowCard(sampleMovies.first()) { }
    }
}