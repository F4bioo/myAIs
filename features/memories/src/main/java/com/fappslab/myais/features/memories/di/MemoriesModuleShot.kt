package com.fappslab.myais.features.memories.di

import com.fappslab.myais.core.navigation.MemoriesNavigation
import com.fappslab.myais.features.memories.navigation.MemoriesNavigationImpl
import com.fappslab.myais.libraries.arch.koin.koinshot.KoinShot
import com.fappslab.myais.libraries.arch.navigation.extension.provideFeatureRoute
import org.koin.core.module.Module
import org.koin.dsl.module

internal class MemoriesModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        provideFeatureRoute<MemoriesNavigation> { MemoriesNavigationImpl() }
    }
}
