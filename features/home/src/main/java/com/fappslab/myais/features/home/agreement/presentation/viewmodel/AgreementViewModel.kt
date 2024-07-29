package com.fappslab.myais.features.home.agreement.presentation.viewmodel

import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.myais.libraries.arch.viewmodel.ViewIntent
import com.fappslab.myais.libraries.arch.viewmodel.ViewModel

internal class AgreementViewModel :
    ViewModel<AgreementViewState, AgreementViewEffect>(AgreementViewState()),
    ViewIntent<AgreementViewIntent> {

    override fun onViewIntent(intent: AgreementViewIntent) {
        when (intent) {
            AgreementViewIntent.OnContinue -> handleContinue()
            AgreementViewIntent.OnOpenSettings -> handleOpenSettings()
            is AgreementViewIntent.OnConditions -> handleConditions(intent.link)
            is AgreementViewIntent.OnPermissionResult -> {
                handlePermissionResult(intent.status)
            }
        }
    }

    private fun handleContinue() {
        onEffect { AgreementViewEffect.NavigateToHome }
    }

    private fun handleOpenSettings() {
        onEffect { AgreementViewEffect.NavigateToSettings }
    }

    private fun handleConditions(link: String) {
        onEffect { AgreementViewEffect.NavigateToConditions(link) }
    }

    private fun handlePermissionResult(status: PermissionStatus) {
        when (status) {
            PermissionStatus.Granted -> onState { it.handlePermissionResultState(status) }
            PermissionStatus.AlwaysDenied -> onState { it.copy(isAlwaysDenied = true) }
            else -> Unit
        }
    }
}
