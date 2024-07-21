package com.fappslab.myais.libraries.arch.koin.koinload

import com.fappslab.myais.libraries.arch.koin.KoinModules
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

abstract class KoinLoad : KoinModules() {

    fun load() {
        loadKoinModules(modules)
    }

    fun unload() {
        unloadKoinModules(modules)
    }
}
