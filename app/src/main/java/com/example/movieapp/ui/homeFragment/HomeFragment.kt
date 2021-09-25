package com.example.movieapp.ui.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.movieapp.ui.cardComposables.ListOfMiniCards
import com.example.movieapp.ui.modifierExtensions.GlassyBox
import com.example.movieapp.ui.theme.*
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

                GlassyBox(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    0.7F,
                    5.dp,
                    MovieAppTheme.colors.primary2
                ) {
                    Text(
                        text = "Popular Movies",
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MovieAppTypography.h5,
                        fontWeight = FontWeight.Bold,
                        color = MovieAppTheme.colors.secondary2
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