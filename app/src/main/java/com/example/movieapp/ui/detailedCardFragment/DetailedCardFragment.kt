package com.example.movieapp.ui.detailedCardFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movieapp.services.model.Movie
import com.example.movieapp.ui.theme.MovieAppTypography
import com.example.movieapp.viewModel.MainViewModel
import kotlinx.coroutines.flow.collect

class DetailedCardFragment : Fragment() {

    private lateinit var controller: NavController
    private val args: DetailedCardFragmentArgs by navArgs()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private val movieResponseState: MutableState<Movie?> = mutableStateOf(null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.fetchMovieById(args.movieId)
                .collect { response -> movieResponseState.value = response }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        controller = this@DetailedCardFragment.findNavController()

        setContent {

            movieResponseState.value
                ?.let { movie -> DetailedCard(movie) }
                ?: run {
                    val action =
                        DetailedCardFragmentDirections.actionDetailedCardFragmentToHomeFragment()
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "Card Details not Found",
                            style = MovieAppTypography.h5,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(
                            onClick = { controller.navigate(action) },
                            modifier = Modifier,
                            content = {
                                Text(
                                    text = "Go back to main view",
                                    style = MovieAppTypography.h5,
                                )
                            }
                        )
                    }
                }
        }
    }
}