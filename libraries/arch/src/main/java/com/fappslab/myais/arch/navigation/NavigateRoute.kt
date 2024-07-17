package com.fappslab.myais.arch.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator

interface NavigateRoute<T : Any> {
    fun navigateToFeature(
        navController: NavHostController,
        route: T,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        navController.navigate(route, navOptions, navigatorExtras)
    }

    fun navigateToFeature(
        navController: NavHostController,
        route: T,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        navController.navigate(route, builder)
    }
}
