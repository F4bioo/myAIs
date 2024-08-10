package com.fappslab.myais.features.home.agreement.navigation

import androidx.navigation.NavGraphBuilder
import com.fappslab.myais.core.navigation.AgreementNavigation
import com.fappslab.myais.core.navigation.AgreementRoute
import com.fappslab.myais.libraries.arch.navigation.extension.animatedComposable
import com.fappslab.myais.features.home.agreement.presentation.compose.AgreementScreen

internal class AgreementNavigationImpl : AgreementNavigation {

    override fun register(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.animatedComposable<AgreementRoute> {
            AgreementScreen()
        }
    }
}
