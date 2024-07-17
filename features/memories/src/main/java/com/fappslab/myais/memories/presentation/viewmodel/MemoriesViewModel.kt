package com.fappslab.myais.memories.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.core.navigation.MemoriesRoute
import com.fappslab.myais.arch.viewmodel.ViewIntent
import com.fappslab.myais.arch.viewmodel.ViewModel
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.usecase.DeleteDriveItemUseCase
import com.fappslab.myais.domain.usecase.ListDriveFilesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalCoroutinesApi::class)
internal class MemoriesViewModel(
    args: MemoriesRoute,
    private val listDriveFilesUseCase: ListDriveFilesUseCase,
    private val deleteDriveItemUseCase: DeleteDriveItemUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

) : ViewModel<MemoriesViewState, MemoriesViewEffect>(MemoriesViewState(args.aspectRatio)),
    ViewIntent<MemoriesViewIntent> {

    override fun onViewIntent(intent: MemoriesViewIntent) {
        when (intent) {
            MemoriesViewIntent.OnInitView -> handleListDriveFiles()
            MemoriesViewIntent.OnBackClicked -> handleBackClicked()
            MemoriesViewIntent.OnLogoutClicked -> handleLogoutClicked()
            is MemoriesViewIntent.OnShowDeleteDialog -> handleShowDeleteDialog(intent.fileId)
            is MemoriesViewIntent.OnShowDownloadDialog -> handleDownloadClicked(intent.memory)
            is MemoriesViewIntent.OnDeleteMemory -> handleDeleteDriveItem(intent.fileId)
        }
    }

    private fun handleBackClicked() {
        onEffect { MemoriesViewEffect.NavigateToHome }
    }

    private fun handleListDriveFiles() {
        listDriveFilesUseCase()
            .flowOn(dispatcher)
            .onStart { onState { it.copy(shouldShowLoading = true) } }
            .catch { listDriveFilesFailure(cause = it) }
            .onCompletion { onState { it.copy(shouldShowLoading = false) } }
            .onEach { listDriveFilesSuccess(memories = it) }
            .launchIn(viewModelScope)
    }

    private fun listDriveFilesFailure(cause: Throwable) {
        cause.printStackTrace()
    }

    private fun listDriveFilesSuccess(memories: Memories) {
        onState { it.copy(owner = memories.owner, memories = memories.list) }
    }

    private fun handleDeleteDriveItem(fileId: String) {
        deleteDriveItemUseCase(DriverItemType.File(fileId))
            .flowOn(dispatcher)
            .onStart { onState { it.copy(shouldShowLoading = true) } }
            .flatMapConcat { listDriveFilesUseCase() }
            .catch { deleteDriveItemFailure(cause = it) }
            .onCompletion { onState { it.copy(shouldShowLoading = false) } }
            .onEach { listDriveFilesSuccess(memories = it) }
            .launchIn(viewModelScope)
    }

    private fun deleteDriveItemFailure(cause: Throwable) {
        cause.printStackTrace()
    }

    private fun handleLogoutClicked() {
        onState { it.copy(shouldShowLoading = true) }
        onEffect { MemoriesViewEffect.Logout }
    }

    private fun handleShowDeleteDialog(fileId: String) {
        onEffect { MemoriesViewEffect.ShowDeleteDialog(fileId) }
    }

    private fun handleDownloadClicked(memory: Memory) {
        onEffect { MemoriesViewEffect.NavigateToDownload(memory) }
    }
}
