package com.fappslab.myais.features.memories.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.fappslab.myais.core.navigation.MemoriesNavigation
import com.fappslab.myais.core.navigation.MemoriesRoute
import com.fappslab.myais.libraries.arch.navigation.extension.animatedComposable
import com.fappslab.myais.features.memories.presentation.compose.MemoriesScreen

internal class MemoriesNavigationImpl : MemoriesNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<MemoriesRoute> {
            val args = it.toRoute<MemoriesRoute>()
            MemoriesScreen(args = args)
        }
    }
}
