package com.fappslab.myais.core.domain.repository

import com.fappslab.myais.core.domain.model.NetworkStateType
import kotlinx.coroutines.flow.Flow

interface NetworkMonitorRepository {
    fun watchNetworkState(): Flow<NetworkStateType>
}
