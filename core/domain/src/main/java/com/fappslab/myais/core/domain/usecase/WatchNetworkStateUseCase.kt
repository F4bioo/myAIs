package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.model.NetworkStateType
import com.fappslab.myais.core.domain.repository.NetworkMonitorRepository
import kotlinx.coroutines.flow.Flow

class WatchNetworkStateUseCase(
    private val repository: NetworkMonitorRepository
) {

    operator fun invoke(): Flow<NetworkStateType> {
        return repository.watchNetworkState()
    }
}
