package com.fappslab.myais.memories.presentation.viewmodel

import com.fappslab.myais.domain.model.Memory

internal sealed interface MemoriesViewEffect {
    data object Logout : MemoriesViewEffect
    data object NavigateToHome : MemoriesViewEffect
    data class ShowDeleteDialog(val fileId: String) : MemoriesViewEffect
    data class NavigateToDownload(val memory: Memory) : MemoriesViewEffect
}