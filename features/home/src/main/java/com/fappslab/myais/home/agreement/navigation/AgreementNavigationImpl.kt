package com.fappslab.myais.home.agreement.navigation

import androidx.navigation.NavGraphBuilder
import com.fappslab.core.navigation.AgreementNavigation
import com.fappslab.core.navigation.AgreementRoute
import com.fappslab.myais.arch.navigation.extension.animatedComposable
import com.fappslab.myais.home.agreement.presentation.compose.AgreementScreen

internal class AgreementNavigationImpl : AgreementNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<AgreementRoute> {
            AgreementScreen()
        }
    }
}
