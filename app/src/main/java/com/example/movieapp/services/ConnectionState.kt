package com.example.movieapp.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/*
* Code from Article linked below:
* https://medium.com/scalereal/observing-live-connectivity-status-in-jetpack-compose-way-f849ce8431c7
* */

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}

private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionState {

    val hasInternetAccess = connectivityManager
        .getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false

    /*val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }*/

    return when (hasInternetAccess) {
        true -> ConnectionState.Available
        false -> ConnectionState.Unavailable
    }
}

val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

@OptIn(ExperimentalCoroutinesApi::class)
fun Context.observeConnectivityAsFlow(): Flow<ConnectionState> = callbackFlow {
    val connectivityManager = getSystemService(ConnectivityManager::class.java)
    val callback = networkCallback { connectionState -> trySend(connectionState) }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)
    connectivityManager.registerDefaultNetworkCallback(callback)

    /* Get initial connectivity status */
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}

fun networkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            callback(ConnectionState.Unavailable)
        }
    }
}