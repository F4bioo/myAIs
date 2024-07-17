package com.fappslab.myais.home.main.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.fappslab.myais.arch.extension.toBase64
import com.fappslab.myais.arch.viewmodel.ViewIntent
import com.fappslab.myais.arch.viewmodel.ViewModel
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.PromptType
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.domain.usecase.CreateContentUseCase
import com.fappslab.myais.domain.usecase.GetPromptUseCase
import com.fappslab.myais.domain.usecase.UploadDriveFileUseCase
import com.fappslab.myais.home.main.presentation.model.AuthType
import com.fappslab.myais.home.main.presentation.model.FailureType
import com.fappslab.myais.home.main.presentation.model.MainStateType
import com.fappslab.myais.remote.network.exception.model.HttpThrowable
import com.fappslab.myais.remote.network.exception.model.InternetThrowable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModel(
    private val getPromptUseCase: GetPromptUseCase,
    private val createContentUseCase: CreateContentUseCase,
    private val uploadDriveFileUseCase: UploadDriveFileUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()),
    ViewIntent<HomeViewIntent> {

    override fun onViewIntent(intent: HomeViewIntent) {
        when (intent) {
            HomeViewIntent.OnNavigateToCamera -> handleNavigateToCamera()
            HomeViewIntent.OnFailureModalClose -> handleFailureModalClose()
            HomeViewIntent.OnGoogleAuthMemories -> handleGoogleAuthMemories()
            is HomeViewIntent.OnGoogleAuth -> handleGoogleAuth(intent.authType)
            is HomeViewIntent.OnSaveMemory -> handleSaveMemory(intent.saveMemory)
            is HomeViewIntent.OnTakePicture -> handleTakePicture(intent.imageBitmap)
            is HomeViewIntent.OnBackHandler -> handleBackHandler(intent.mainStateType)
            is HomeViewIntent.OnGoogleAuthMemory -> handleGoogleAuthMemory(intent.saveMemory)
            is HomeViewIntent.OnFailureModalRetry -> handleFailureModalRetry(intent.failureType)
        }
    }

    private fun handleGoogleAuth(authType: AuthType) {
        when (authType) {
            AuthType.NavigateToMemories -> {
                onEffect { HomeViewEffect.NavigateToMemories(viewState.ratioType) }
            }

            AuthType.UploadMemory -> viewState.saveMemory?.let(::handleSaveMemory)
        }
    }

    private fun handleFailureModalClose() {
        onState { it.copy(shouldShowFailure = false) }
    }

    private fun handleSaveMemory(saveMemory: SaveMemory) {
        uploadDriveFileUseCase(saveMemory)
            .flowOn(dispatcher)
            .onStart { onState { it.handleSaveMemoryStartState() } }
            .catch { handleSaveMemoryFailure(cause = it) }
            .onEach { onState { it.handleSaveMemorySuccessState() } }
            .launchIn(viewModelScope)
    }

    private fun handleSaveMemoryFailure(cause: Throwable) {
        val failureType = FailureType.UploadError
        onState { it.handleSaveMemoryFailureState(failureType) }
        cause.printStackTrace()
    }

    private fun handleNavigateToCamera() {
        onState { it.handleNavigateToCameraState() }
    }

    private fun handleTakePicture(imageBitmap: Bitmap) {
        onState { it.copy(imageBitmap = imageBitmap) }
        val encodedImage = imageBitmap.toBase64()
        getDescription(encodedImage)
    }

    private fun getDescription(encodedImage: String) {
        getPromptUseCase(PromptType.ImageDescription)
            .flowOn(dispatcher)
            .onStart { onState { it.getDescriptionStartState() } }
            .flatMapConcat { textPrompt ->
                createContentUseCase { text(textPrompt); image(encodedImage) }
            }
            .catch { getDescriptionFailure(cause = it) }
            .onEach { getDescriptionSuccess(imageDescription = it) }
            .launchIn(viewModelScope)
    }

    private fun getDescriptionFailure(cause: Throwable) {
        val failureType = when (cause) {
            is InternetThrowable -> FailureType.ConnectionError
            is HttpThrowable -> FailureType.AnalyzeError
            else -> FailureType.GenericError

        }
        onState { it.getDescriptionFailureState(failureType) }
        cause.printStackTrace()
    }

    private fun getDescriptionSuccess(imageDescription: Description) {
        onState { it.getDescriptionSuccessState(imageDescription) }
    }

    private fun handleBackHandler(mainStateType: MainStateType) {
        if (mainStateType == MainStateType.Preview) {
            handleNavigateToCamera()
        }
    }

    private fun handleGoogleAuthMemories() {
        onEffect { HomeViewEffect.CheckAuthMemories(viewState.ratioType) }
    }

    private fun handleGoogleAuthMemory(saveMemory: SaveMemory) {
        onState { it.copy(saveMemory = saveMemory) }
        onEffect { HomeViewEffect.CheckAuthMemory(saveMemory) }
    }

    private fun handleFailureModalRetry(failureType: FailureType) {
        when (failureType) {
            FailureType.GenericError,
            FailureType.ConnectionError,
            FailureType.AnalyzeError -> {
                viewState.imageBitmap?.let { imageBitmap ->
                    val encodedImage = imageBitmap.toBase64()
                    getDescription(encodedImage)
                }
            }

            else -> Unit
        }
    }
}
