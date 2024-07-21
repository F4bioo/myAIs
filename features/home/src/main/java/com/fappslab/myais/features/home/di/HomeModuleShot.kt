package com.fappslab.myais.features.home.di

import com.fappslab.myais.core.navigation.AgreementNavigation
import com.fappslab.myais.core.navigation.HomeNavigation
import com.fappslab.myais.libraries.arch.koin.koinshot.KoinShot
import com.fappslab.myais.libraries.arch.navigation.extension.provideFeatureRoute
import com.fappslab.myais.features.home.agreement.navigation.AgreementNavigationImpl
import com.fappslab.myais.features.home.main.navigation.HomeNavigationImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal class HomeModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        provideFeatureRoute<HomeNavigation> { HomeNavigationImpl() }
        provideFeatureRoute<AgreementNavigation> { AgreementNavigationImpl() }
    }
}
