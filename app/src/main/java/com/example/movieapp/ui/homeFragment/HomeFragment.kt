package com.example.movieapp.ui.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.movieapp.ui.cardComposables.ListOfMiniCards
import com.example.movieapp.ui.theme.MovieAppTypography
import com.example.movieapp.viewModel.MainViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private lateinit var controller: NavController
    private val sharedViewModel by sharedViewModel<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.fetchLatestMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        controller = this@HomeFragment.findNavController()

        val addMoreOnScrollEndFun = {
            viewLifecycleOwner.lifecycleScope.launch {
                /*delay(5_000)*/ /* Time to display Loader image */
                sharedViewModel.fetchLatestMoviesNextPage()
            }
        }

        val viewDetailedMovieFun = { movieId: Int ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movieId)
            controller.navigate(action)
        }

        setContent {
            Column {
                OutlinedButton(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.12F),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Popular Movies",
                        textAlign = TextAlign.Center,
                        style = MovieAppTypography.h6
                    )
                }

                ListOfMiniCards(
                    sharedViewModel.movieList,
                    addMoreOnScrollEndFun,
                    viewDetailedMovieFun
                )
            }
        }
    }
}