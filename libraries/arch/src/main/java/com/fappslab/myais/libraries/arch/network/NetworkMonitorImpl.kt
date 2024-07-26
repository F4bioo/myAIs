package com.fappslab.myais.libraries.arch.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

private const val HOST_NAME = "8.8.8.8"
private const val PORT = 53
private const val TIMEOUT_MILLIS = 1500

internal class NetworkMonitorImpl(
    context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkMonitor {

    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    override fun isOnline(): Flow<Boolean> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                checkRealConnection(this@callbackFlow)
            }

            override fun onLost(network: Network) {
                trySend(false).isSuccess
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                checkRealConnection(this@callbackFlow)
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        checkRealConnection(this@callbackFlow)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun checkRealConnection(callbackFlowScope: ProducerScope<Boolean>) {
        CoroutineScope(dispatcher).launch {
            val hasInternet = runCatching {
                Socket().use { socket ->
                    socket.connect(
                        InetSocketAddress(HOST_NAME, PORT),
                        TIMEOUT_MILLIS
                    )
                    true
                }
            }.getOrDefault(false)

            callbackFlowScope.trySend(hasInternet).isSuccess
        }
    }
}
