package com.fappslab.myais.arch.koin

import org.koin.core.module.Module
import org.koin.dsl.module

abstract class KoinModules {

    val modules: List<Module>
        get() = listOf(domainModule, dataModule, presentationModule, additionalModule)

    protected open val domainModule: Module = module { }

    protected open val dataModule: Module = module { }

    protected open val presentationModule: Module = module { }

    protected open val additionalModule: Module = module { }
}
