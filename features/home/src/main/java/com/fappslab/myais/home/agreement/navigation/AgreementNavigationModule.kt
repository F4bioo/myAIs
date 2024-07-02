package com.fappslab.myais.home.agreement.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fappslab.core.navigation.AgreementNavigation
import com.fappslab.myais.home.agreement.presentation.compose.AgreementScreen
import org.koin.compose.koinInject

internal class AgreementNavigationImpl : AgreementNavigation {

    override val route: String
        get() = "agreement_screen"

    override fun navigateToAgreement(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable(route = route) {
            AgreementScreen(navController = navController, homeNavigation = koinInject())
        }
    }
}
