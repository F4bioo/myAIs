package com.fappslab.myais.features.home.agreement.presentation.viewmodel

import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus

sealed interface AgreementViewIntent {
    data object OnContinue : AgreementViewIntent
    data object OnPrivacyPolicy : AgreementViewIntent
    data object OnOpenSettings : AgreementViewIntent
    data class OnPermissionResult(val status: PermissionStatus) : AgreementViewIntent
}
