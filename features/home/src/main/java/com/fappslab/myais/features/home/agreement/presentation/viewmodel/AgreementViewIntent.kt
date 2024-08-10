package com.fappslab.myais.features.home.agreement.presentation.viewmodel

import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus

sealed interface AgreementViewIntent {
    data object OnContinue : AgreementViewIntent
    data object OnOpenSettings : AgreementViewIntent
    data class OnConditions(val link: String) : AgreementViewIntent
    data class OnPermissionResult(val status: PermissionStatus) : AgreementViewIntent
}
