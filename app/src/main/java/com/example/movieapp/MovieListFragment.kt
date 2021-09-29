package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.annotation.ExperimentalCoilApi
import com.example.movieapp.services.model.sampleMovies
import com.example.movieapp.ui.modifierExtensions.dividerColor
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography

class MovieListFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(Modifier) {
                    sampleMovies.forEach { movieData ->
                        item {
                            Divider(modifier = Modifier.padding(horizontal = 10.dp), color = dividerColor)
                            Row(Modifier) {
                                Image(
                                    painter = painterResource(id = R.drawable.icons_circled_male),
                                    contentDescription = "Cover Image ${movieData.title}",
                                    modifier = Modifier
                                        .weight(0.3F)
                                        .padding(10.dp)
                                        .background(Color.Transparent)
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(5.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    Modifier
                                        .weight(0.7F)
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "${movieData.title} (${movieData.release_date.year})",
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MovieAppTypography.h6,
                                        color = MovieAppColorTheme.colors.secondary1
                                    )
                                    Text(
                                        text = movieData.genres.toString(),
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MovieAppTypography.body1,
                                        color = MovieAppColorTheme.colors.secondary2
                                    )
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 10.dp), color = dividerColor)
                        }
                    }
                }
            }
        }
    }
}