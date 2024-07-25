package com.fappslab.myais.features.memories.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.fappslab.myais.core.data.remote.api.FETCH_LIMIT
import com.fappslab.myais.core.domain.model.DriverItemType
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.core.domain.usecase.DeleteDriveItemUseCase
import com.fappslab.myais.core.domain.usecase.GetDriveOwnerUseCase
import com.fappslab.myais.core.navigation.MemoriesRoute
import com.fappslab.myais.features.memories.data.paging.MemoriesPagingDataFactory
import com.fappslab.myais.libraries.arch.viewmodel.ViewIntent
import com.fappslab.myais.libraries.arch.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class MemoriesViewModel(
    args: MemoriesRoute,
    private val pagingDataFactory: MemoriesPagingDataFactory,
    private val getDriveOwnerUseCase: GetDriveOwnerUseCase,
    private val deleteDriveItemUseCase: DeleteDriveItemUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

) : ViewModel<MemoriesViewState, MemoriesViewEffect>(MemoriesViewState(args.aspectRatio)),
    ViewIntent<MemoriesViewIntent> {

    override fun onViewIntent(intent: MemoriesViewIntent) {
        when (intent) {
            MemoriesViewIntent.OnInitView -> handleGetDriveOwner()
            MemoriesViewIntent.OnBackClicked -> handleBackClicked()
            MemoriesViewIntent.OnLogoutClicked -> handleLogoutClicked()
            MemoriesViewIntent.OnFailureModalClose -> handleFailureModalClose()
            is MemoriesViewIntent.OnShowDeleteDialog -> handleShowDeleteDialog(intent.fileId)
            is MemoriesViewIntent.OnShowDownloadDialog -> handleDownloadClicked(intent.memory)
            is MemoriesViewIntent.OnLoadStates -> handleCombinedLoad(intent.pagingItems)
            is MemoriesViewIntent.OnDeleteMemory -> handleDeleteDriveItem(intent.fileId)
            is MemoriesViewIntent.OnFailureModalRetry -> handleFailureModalRetry(intent.failureType)
        }
    }

    private fun handleBackClicked() {
        onEffect { MemoriesViewEffect.NavigateToHome }
    }

    private fun handleGetDriveOwner() {
        getDriveOwnerUseCase()
            .flowOn(dispatcher)
            .onStart { onState { it.copy(shouldShowLoading = true, failureType = null) } }
            .catch { getDriveOwnerFailure(cause = it) }
            .onEach { getDriveOwnerSuccess(owner = it) }
            .launchIn(viewModelScope)
    }

    private fun getDriveOwnerSuccess(owner: Owner) {
        onState { it.copy(owner = owner) }
        listDriveFiles()
    }

    private fun getDriveOwnerFailure(cause: Throwable) {
        cause.printStackTrace()
        onState { it.getDriveOwnerFailureState() }
    }

    private fun listDriveFiles() {
        val memories = pagingDataFactory.create(FETCH_LIMIT)
            .flowOn(dispatcher)
            .cachedIn(viewModelScope)
        onState { it.copy(memories = memories) }
    }

    private fun handleDeleteDriveItem(fileId: String?) {
        deleteDriveItemUseCase(DriverItemType.File(fileId.orEmpty()))
            .flowOn(dispatcher)
            .onStart { onState { it.copy(shouldShowLoading = true, fileId = fileId) } }
            .catch { deleteDriveItemFailure(cause = it) }
            .onEach { listDriveFiles() }
            .launchIn(viewModelScope)
    }

    private fun deleteDriveItemFailure(cause: Throwable) {
        cause.printStackTrace()
        onState { it.copy(shouldShowLoading = false, failureType = FailureType.DeleteDriveItem) }
    }

    private fun handleLogoutClicked() {
        onState { it.copy(shouldShowLoading = true) }
        onEffect { MemoriesViewEffect.Logout }
    }

    private fun handleShowDeleteDialog(fileId: String?) {
        onEffect { MemoriesViewEffect.ShowDeleteDialog(fileId) }
    }

    private fun handleDownloadClicked(memory: Memory) {
        onEffect { MemoriesViewEffect.NavigateToDownload(memory) }
    }

    private fun handleFailureModalClose() {
        onState { it.copy(failureType = null) }
    }

    private fun handleFailureModalRetry(failureType: FailureType?) {
        when (failureType) {
            FailureType.ListDriveFiles -> handleGetDriveOwner()
            FailureType.DeleteDriveItem -> handleDeleteDriveItem(viewState.fileId)
            else -> Unit
        }
    }

    private fun handleCombinedLoad(pagingItems: LazyPagingItems<Memory>) {
        val isLoading = pagingItems.loadState.refresh is LoadState.Loading
        val isEmpty = pagingItems.itemCount == 0 && !isLoading
        onState { it.copy(shouldShowLoading = isLoading, shouldShowEmptyScreen = isEmpty) }
    }
}
