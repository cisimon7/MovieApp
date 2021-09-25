package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.movieapp.ui.modifierExtensions.GlassyBox
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.MovieAppTypography

class MovieListFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

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
                GlassyBox(murkiness = 0.5F, cornerRadius = 10.dp) {
                    Text(
                        text = "Movie List Fragment",
                        style = MovieAppTypography.h4,
                        modifier = Modifier.padding(10.dp),
                        color = MovieAppTheme.colors.secondary2
                    )
                }
            }
        }
    }
}