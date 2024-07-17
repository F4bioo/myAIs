package com.fappslab.myais.home.main.navigation

import androidx.navigation.NavGraphBuilder
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.core.navigation.HomeRoute
import com.fappslab.myais.arch.navigation.extension.animatedComposable
import com.fappslab.myais.home.main.presentation.compose.HomeScreen

internal class HomeNavigationImpl : HomeNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<HomeRoute> {
            HomeScreen()
        }
    }
}
