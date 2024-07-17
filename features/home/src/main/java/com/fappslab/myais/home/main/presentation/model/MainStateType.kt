package com.fappslab.myais.home.main.presentation.model

internal sealed interface MainStateType {
    data object Camera : MainStateType
    data object Analyze : MainStateType
    data object Preview : MainStateType
}
