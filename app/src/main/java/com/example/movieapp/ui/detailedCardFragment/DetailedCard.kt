package com.example.movieapp.ui.detailedCardFragment

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieapp.R
import com.example.movieapp.services.model.Movie
import com.example.movieapp.ui.cardComposables.RatingCircleItem
import com.example.movieapp.ui.theme.MovieAppTypography

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DetailedCard(movieData: Movie) {

    val scrollState = rememberScrollState(0)

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        val paddingValue = 15.dp

        Image(
            painter = rememberImagePainter (
                data = movieData.cover_url,
                builder = {
                    placeholder(R.drawable.image_placeholder_1)
                    crossfade(1_000)
                }
            ),
            contentDescription = "Cover Image ${movieData.title}",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE5E8E8))
                .height(500.dp)
                .clip(RoundedCornerShape(3.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValue)
        ) {
            Text(
                text = "${movieData.title} (${movieData.release_date.year})",
                modifier = Modifier.fillMaxWidth(),
                style = MovieAppTypography.h4
            )
            Text(
                text = movieData.genres.joinToString(", "),
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                style = MovieAppTypography.h6
            )
        }

        Divider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                RatingCircleItem(movieData.vote_average, Modifier.size(60.dp))
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "User\nScore", style = MovieAppTypography.h5)
            }
            FavoriteIcon()
            Icon(
                painter = painterResource(id = R.drawable.icons_bookmark),
                contentDescription = "Bookmark",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
            Icon(
                painter = painterResource(id = R.drawable.icons_list_52px),
                contentDescription = "Options",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        }

        Divider()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValue)
        ) {
            Text(
                text = "Overview",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                style = MovieAppTypography.h5
            )
            Text(
                text = movieData.overview,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                style = MovieAppTypography.body1
            )
        }

        ReviewDiscussionDropDown(
            modifier = Modifier
                .padding(horizontal = paddingValue * 0.5F)
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = 0.12F),
                    shape = RoundedCornerShape(5.dp)
                )
        )
    }

}

@Composable
private fun FavoriteIcon() {
    var isLiked by remember { mutableStateOf(false) }
    val transition = updateTransition(isLiked, label = "")

    val scale by transition.animateFloat(
        transitionSpec = { repeatable(2, tween(300), RepeatMode.Reverse) },
        targetValueByState = { state ->
            when (state) {
                true -> 1.5F
                false -> 1F
            }
        }, label = ""
    )
    Icon(
        painter = painterResource(id = R.drawable.icons_heart_outline),
        contentDescription = "Like Movie",
        modifier = Modifier
            .size(30.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { isLiked = !isLiked },
        tint = if (isLiked) Color.Red else Color.Black
    )
}