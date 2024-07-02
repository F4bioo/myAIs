package com.fappslab.core.navigation.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavigationModule {
    val route: String
    fun navigateToFeature(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
}
