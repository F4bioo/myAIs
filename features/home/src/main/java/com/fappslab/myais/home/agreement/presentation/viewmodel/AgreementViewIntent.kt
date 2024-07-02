package com.fappslab.myais.home.agreement.presentation.viewmodel

import com.fappslab.myais.arch.simplepermission.model.PermissionStatus

sealed class AgreementViewIntent {
    data object OnContinue : AgreementViewIntent()
    data object OnPrivacyPolicy : AgreementViewIntent()
    data object OnOpenSettings : AgreementViewIntent()
    data class OnPermissionResult(val status: PermissionStatus) : AgreementViewIntent()
}
