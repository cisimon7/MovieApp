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
import com.example.movieapp.ui.modifierExtensions.MiniCardLayout
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow


/*Features to add
* * onLong press, display tagline and below display display overview details*/

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
        when {
            loadState.refresh is LoadState.Loading -> {
                Text(text = "Refreshing", style = MovieAppTypography.h6)
            }
            loadState.append !is LoadState.Loading -> {
                isUpdating = false
            }
            loadState.append is LoadState.Loading -> {
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
                            ?.let { ShowCard(it, displayMovieDetails) }
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
            .padding(3.dp)
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
        elevation = 10.dp,
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                Box(modifier = Modifier.weight(0.9F)) {
                    MiniCardLayout(Modifier) {
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
                                .background(Color(0xFFE5E8E8)),
                            contentScale = ContentScale.Crop
                        )
                        RatingCircleItem(movieData.vote_average, Modifier.size(40.dp))
                        /*
                        * more options:
                        * Add to List (List)
                        * Favorite (Love)
                        * Watchlist (Bookmark)
                        * Your rating (Star)*/
                        Box(modifier = Modifier)
                    }
                }

                Column(
                    Modifier
                        .padding(5.dp)
                        .weight(0.2F)
                ) {
                    Text(
                        text = movieData.title,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 10.sp,
                        style = MovieAppTypography.h3
                    )
                    Text(
                        text = movieData.release_date.toString(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 7.sp,
                        color = Color.Gray,
                        style = MovieAppTypography.h5
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun ShowCardPreview() {
    ShowCard(sampleMovies.first()) { }
}