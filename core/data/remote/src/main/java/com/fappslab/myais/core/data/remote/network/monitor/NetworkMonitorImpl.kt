package com.fappslab.myais.core.data.remote.network.monitor

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.fappslab.myais.core.domain.model.NetworkStateType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val PING_COMMAND = "/system/bin/ping -c 4 -W 1 8.8.8.8"
private const val DEBOUNCE_TIME = 500L

internal class NetworkMonitorImpl(
    private val connectivityManager: ConnectivityManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val checkRealConnection: (ProducerScope<NetworkStateType>) -> Job = { scope ->
        checkRealConnection(dispatcher, scope)
    }
) : NetworkMonitor {

    private var job: Job? = null

    @OptIn(FlowPreview::class)
    override fun watchNetworkState(): Flow<NetworkStateType> = callbackFlow {
        val networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                job?.cancel()
                job = checkRealConnection(this@callbackFlow)
            }

            override fun onUnavailable() {
                trySend(NetworkStateType.Unavailable).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(NetworkStateType.Lost).isSuccess
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                job?.cancel()
                job = checkRealConnection(this@callbackFlow)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest(), networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            job?.cancel()
        }

    }.debounce(DEBOUNCE_TIME).distinctUntilChanged()

    private fun networkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            .build()
    }
}

internal fun checkRealConnection(
    dispatcher: CoroutineDispatcher,
    callbackFlowScope: ProducerScope<NetworkStateType>
): Job = CoroutineScope(dispatcher).launch {
    val hasInternet = runCatching {
        val process = Runtime.getRuntime().exec(PING_COMMAND)
        val exitValue = process.waitFor()
        exitValue == 0
    }.getOrDefault(defaultValue = false)

    callbackFlowScope.trySend(
        if (hasInternet) {
            NetworkStateType.Available
        } else NetworkStateType.Unavailable
    ).isSuccess
}
