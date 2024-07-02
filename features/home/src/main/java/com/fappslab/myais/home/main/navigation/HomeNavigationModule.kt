package com.fappslab.myais.home.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.myais.home.main.presentation.compose.HomeScreen
import org.koin.compose.koinInject

internal class HomeNavigationImpl : HomeNavigation {

    override val route: String = "home_screen"

    override fun navigateToFeature(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable(route = route) {
            HomeScreen(navController = navController, memoriesNavigation = koinInject())
        }
    }
}
