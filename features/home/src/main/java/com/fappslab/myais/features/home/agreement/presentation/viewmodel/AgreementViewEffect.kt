package com.fappslab.myais.features.home.agreement.presentation.viewmodel

sealed interface AgreementViewEffect {
    data object NavigateToHome : AgreementViewEffect
    data object NavigateToSettings : AgreementViewEffect
    data class NavigateToConditions(val link: String) : AgreementViewEffect
}
