package com.example.movieapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movieapp.databinding.ContentMainBinding
import com.example.movieapp.services.ConnectionState
import com.example.movieapp.services.observeConnectivityAsFlow
import com.example.movieapp.ui.ConnectivityComposable
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.viewModel.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MainViewModel>()

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

            MovieAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(Modifier.fillMaxSize()) {
                        ConnectivityComposable(connectionState.value)
                        AndroidViewBinding(ContentMainBinding::inflate)
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