package com.fappslab.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface HomeNavigation {
    val route: String
    fun navigateToHome(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
}
