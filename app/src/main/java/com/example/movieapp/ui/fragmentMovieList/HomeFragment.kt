package com.example.movieapp.ui.fragmentMovieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.movieapp.ui.theme.*
import com.example.movieapp.viewModel.MainViewModel
import kotlinx.coroutines.delay
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
                delay(10_000) /* Time to display Loader image visible */
                sharedViewModel.fetchLatestMoviesNextPage()
            }
        }

        val viewDetailedMovieFun = { movieId: Int ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movieId)
            controller.navigate(action)
        }

        setContent {
            MiniMovieCardListComposable(
                sharedViewModel.movieList,
                addMoreOnScrollEndFun,
                viewDetailedMovieFun
            )
        }
    }
}