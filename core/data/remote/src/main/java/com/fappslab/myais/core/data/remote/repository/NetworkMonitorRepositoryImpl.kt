package com.fappslab.myais.core.data.remote.repository

import com.fappslab.myais.core.data.remote.network.monitor.NetworkMonitor
import com.fappslab.myais.core.domain.model.NetworkStateType
import com.fappslab.myais.core.domain.repository.NetworkMonitorRepository
import kotlinx.coroutines.flow.Flow

internal class NetworkMonitorRepositoryImpl(
    private val networkMonitor: NetworkMonitor
) : NetworkMonitorRepository {

    override fun watchNetworkState(): Flow<NetworkStateType> {
        return networkMonitor.watchNetworkState()
    }
}
