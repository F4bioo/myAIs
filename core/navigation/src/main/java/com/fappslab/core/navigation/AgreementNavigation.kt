package com.fappslab.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface AgreementNavigation {
    val route: String
    fun navigateToAgreement(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
}
