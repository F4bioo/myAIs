package com.fappslab.myais.features.memories.di

import com.fappslab.myais.core.domain.usecase.DeleteDriveItemUseCase
import com.fappslab.myais.core.domain.usecase.GetDriveOwnerUseCase
import com.fappslab.myais.core.navigation.MemoriesNavigation
import com.fappslab.myais.core.navigation.MemoriesRoute
import com.fappslab.myais.features.memories.data.paging.MemoriesPagingDataFactoryImpl
import com.fappslab.myais.features.memories.navigation.MemoriesNavigationImpl
import com.fappslab.myais.features.memories.presentation.viewmodel.MemoriesViewModel
import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.myais.libraries.arch.koin.koinshot.KoinShot
import com.fappslab.myais.libraries.arch.navigation.extension.provideFeatureRoute
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object MemoriesModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (args: MemoriesRoute) ->
            MemoriesViewModel(
                args = args,
                pagingDataFactory = MemoriesPagingDataFactoryImpl(repository = get()),
                getDriveOwnerUseCase = GetDriveOwnerUseCase(repository = get()),
                deleteDriveItemUseCase = DeleteDriveItemUseCase(repository = get()),
            )
        }
    }
}

internal class MemoriesModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        provideFeatureRoute<MemoriesNavigation> { MemoriesNavigationImpl() }
    }
}
