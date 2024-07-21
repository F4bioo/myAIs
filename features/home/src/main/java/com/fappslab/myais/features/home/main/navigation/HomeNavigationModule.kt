package com.fappslab.myais.features.home.main.navigation

import androidx.navigation.NavGraphBuilder
import com.fappslab.myais.core.navigation.HomeNavigation
import com.fappslab.myais.core.navigation.HomeRoute
import com.fappslab.myais.libraries.arch.navigation.extension.animatedComposable
import com.fappslab.myais.features.home.main.presentation.compose.HomeScreen

internal class HomeNavigationImpl : HomeNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<HomeRoute> {
            HomeScreen()
        }
    }
}
