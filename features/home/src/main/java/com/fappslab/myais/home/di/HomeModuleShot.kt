package com.fappslab.myais.home.di

import com.fappslab.core.navigation.AgreementNavigation
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.arch.navigation.extension.provideFeatureRoute
import com.fappslab.myais.home.agreement.navigation.AgreementNavigationImpl
import com.fappslab.myais.home.main.navigation.HomeNavigationImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal class HomeModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        provideFeatureRoute<HomeNavigation> { HomeNavigationImpl() }
        provideFeatureRoute<AgreementNavigation> { AgreementNavigationImpl() }
    }
}
