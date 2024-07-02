package com.fappslab.myais.home.agreement.presentation.viewmodel

sealed interface AgreementViewEffect {
    data object NavigateToHome : AgreementViewEffect
    data object NavigateToSettings : AgreementViewEffect
    data object NavigateToPrivacyPolicy : AgreementViewEffect
}
