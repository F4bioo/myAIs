package com.fappslab.myais.home.di

import com.fappslab.core.navigation.AgreementNavigation
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.myais.arch.koin.koinload.KoinLoad
import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.home.agreement.navigation.AgreementNavigationImpl
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewModel
import com.fappslab.myais.home.main.navigation.HomeNavigationImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object HomeModuleKoinLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { AgreementViewModel() }
    }
}

internal class HomeModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        factory<HomeNavigation> { HomeNavigationImpl() }
        factory<AgreementNavigation> { AgreementNavigationImpl() }
    }
}
