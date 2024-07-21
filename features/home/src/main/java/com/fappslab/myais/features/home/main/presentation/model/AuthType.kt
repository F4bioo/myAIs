package com.fappslab.myais.features.home.main.presentation.model

internal sealed interface AuthType {
    data object NavigateToMemories : AuthType
    data object UploadMemory : AuthType
}
