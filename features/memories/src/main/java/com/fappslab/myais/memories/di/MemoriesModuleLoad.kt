package com.fappslab.myais.memories.di

import com.fappslab.core.navigation.MemoriesNavigation
import com.fappslab.core.navigation.MemoriesRoute
import com.fappslab.myais.arch.koin.koinload.KoinLoad
import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.arch.navigation.extension.provideFeatureRoute
import com.fappslab.myais.domain.usecase.DeleteDriveItemUseCase
import com.fappslab.myais.domain.usecase.ListDriveFilesUseCase
import com.fappslab.myais.memories.navigation.MemoriesNavigationImpl
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object MemoriesModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (args: MemoriesRoute) ->
            MemoriesViewModel(
                args = args,
                listDriveFilesUseCase = ListDriveFilesUseCase(repository = get()),
                deleteDriveItemUseCase = DeleteDriveItemUseCase(repository = get())
            )
        }
    }
}

internal class MemoriesModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        provideFeatureRoute<MemoriesNavigation> { MemoriesNavigationImpl() }
    }
}
