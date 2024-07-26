package com.fappslab.myais.libraries.arch.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    fun isOnline(): Flow<Boolean>
}
