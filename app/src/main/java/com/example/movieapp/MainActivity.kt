package com.example.movieapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movieapp.databinding.ContentMainBinding
import com.example.movieapp.databinding.SplashScreenBinding
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.services.observeConnectivityAsFlow
import com.example.movieapp.ui.generalComponents.ConnectivityComposable
import com.example.movieapp.ui.scaffoldComponents.MovieAppBar
import com.example.movieapp.ui.scaffoldComponents.MovieAppDrawer
import com.example.movieapp.ui.scaffoldComponents.MovieAppScaffold
import com.example.movieapp.ui.theme.MovieAppColors
import com.example.movieapp.ui.theme.ProvideMovieAppColors
import com.example.movieapp.ui.theme.customDarkTheme
import com.example.movieapp.viewModel.MovieListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MovieListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            applicationContext
                ?.observeConnectivityAsFlow()
                ?.stateIn(lifecycleScope)
                ?.onEach { connectionState: ConnectionState ->
                    viewModel.connectionState.value = connectionState
                }?.launchIn(lifecycleScope)
        }

        setContent {

            val connectionState = viewModel.connectionState.collectAsState()

            var title by remember { mutableStateOf("Movie List") }

            val scope = rememberCoroutineScope()

            val scaffoldState = rememberScaffoldState()

            var themeColors by remember { mutableStateOf(customDarkTheme) }

            val onNavIconPressed: () -> Unit = {
                scope.launch { scaffoldState.drawerState.open() }
            }

            val navToHome: () -> Unit = {
                findNavController().navigate(R.id.action_global_homeFragment)
                title = "Movie List"
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }

            val navToProfile: () -> Unit = {
                findNavController().navigate(R.id.action_global_profileFragment)
                title = "My Profile"
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }

            val navToMovieList: () -> Unit = {
                findNavController().navigate(R.id.action_global_movieListFragment)
                title = "Saved Movies"
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }

            val navToReminderList: () -> Unit = {
                findNavController().navigate(R.id.action_global_reminderFragment)
                title = "Movie Reminders"
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }

            val changeThemeColorTo: (MovieAppColors) -> Unit = { newAppColors: MovieAppColors ->
                themeColors = newAppColors
            }

            ProvideMovieAppColors(colors = themeColors) {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination="SplashScreen") {
                    composable(route = "SplashScreen") {
                        LaunchedEffect(key1 = Unit) {
                            delay(10_000)
                            navController.navigate("MainScreen")
                        }
                        AndroidViewBinding(factory = SplashScreenBinding::inflate)
                    }
                    composable(route = "MainScreen") {
                        MovieAppScaffold(
                            scaffoldState,
                            { MovieAppBar(onNavIconPressed, title) },
                            { MovieAppDrawer(navToHome, navToProfile, navToMovieList, navToReminderList, changeThemeColorTo) },
                            content = {
                                Box(Modifier.fillMaxSize()) {
                                    AndroidViewBinding(factory = ContentMainBinding::inflate)
                                    ConnectivityComposable(connectionState.value)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        return navHostFragment.navController
    }
}