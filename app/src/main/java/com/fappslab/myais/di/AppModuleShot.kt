package com.fappslab.myais.di

import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.domain.usecase.CreateContentUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

class AppModuleShot : KoinShot() {

    override val dataModule: Module = module {
        factory { CreateContentUseCase(repository = get()) }
    }
}
