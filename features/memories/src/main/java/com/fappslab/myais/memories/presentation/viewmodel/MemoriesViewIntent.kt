package com.fappslab.myais.memories.presentation.viewmodel

import com.fappslab.myais.domain.model.Memory

internal sealed interface MemoriesViewIntent {
    data object OnInitView : MemoriesViewIntent
    data object OnBackClicked : MemoriesViewIntent
    data object OnLogoutClicked : MemoriesViewIntent
    data class OnShowDownloadDialog(val memory: Memory) : MemoriesViewIntent
    data class OnShowDeleteDialog(val fileId: String) : MemoriesViewIntent
    data class OnDeleteMemory(val fileId: String) : MemoriesViewIntent
}
