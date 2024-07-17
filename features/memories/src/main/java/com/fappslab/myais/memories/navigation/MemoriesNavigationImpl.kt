package com.fappslab.myais.memories.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.fappslab.core.navigation.MemoriesNavigation
import com.fappslab.core.navigation.MemoriesRoute
import com.fappslab.myais.arch.navigation.extension.animatedComposable
import com.fappslab.myais.memories.presentation.compose.MemoriesScreen

internal class MemoriesNavigationImpl : MemoriesNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<MemoriesRoute> {
            val args = it.toRoute<MemoriesRoute>()
            MemoriesScreen(args = args)
        }
    }
}
