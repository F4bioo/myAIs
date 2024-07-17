package com.fappslab.myais.home.main.presentation.model

internal sealed interface AuthType {
    data object NavigateToMemories : AuthType
    data object UploadMemory : AuthType
}
