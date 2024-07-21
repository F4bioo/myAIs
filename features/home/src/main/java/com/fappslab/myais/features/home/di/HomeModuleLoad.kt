package com.fappslab.myais.features.home.di

import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.myais.core.domain.usecase.CreateContentUseCase
import com.fappslab.myais.core.domain.usecase.GetPromptUseCase
import com.fappslab.myais.core.domain.usecase.UploadDriveFileUseCase
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewModel
import com.fappslab.myais.features.home.main.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object HomeModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { AgreementViewModel() }
        viewModel {
            HomeViewModel(
                getPromptUseCase = GetPromptUseCase(repository = get()),
                createContentUseCase = CreateContentUseCase(repository = get()),
                uploadDriveFileUseCase = UploadDriveFileUseCase(repository = get())
            )
        }
    }
}
