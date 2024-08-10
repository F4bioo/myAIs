package com.fappslab.myais.core.data.remote.network.monitor

import com.fappslab.myais.core.domain.model.NetworkStateType
import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    fun watchNetworkState(): Flow<NetworkStateType>
}
