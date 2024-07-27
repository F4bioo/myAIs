package com.fappslab.myais.features.home.main.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.fappslab.myais.core.data.remote.network.exception.model.HttpThrowable
import com.fappslab.myais.core.data.remote.network.exception.model.InternetThrowable
import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.model.ModelType
import com.fappslab.myais.core.domain.model.PromptType
import com.fappslab.myais.core.domain.model.SaveMemory
import com.fappslab.myais.core.domain.usecase.CreateContentUseCase
import com.fappslab.myais.core.domain.usecase.GetPromptUseCase
import com.fappslab.myais.core.domain.usecase.UploadDriveFileUseCase
import com.fappslab.myais.core.domain.usecase.WatchNetworkStateUseCase
import com.fappslab.myais.features.home.main.presentation.model.AuthType
import com.fappslab.myais.features.home.main.presentation.model.FailureType
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.MainStateType
import com.fappslab.myais.libraries.arch.extension.toBase64
import com.fappslab.myais.libraries.arch.viewmodel.ViewIntent
import com.fappslab.myais.libraries.arch.viewmodel.ViewModel
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
    private val watchNetworkStateUseCase: WatchNetworkStateUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()),
    ViewIntent<HomeViewIntent> {

    override fun onViewIntent(intent: HomeViewIntent) {
        when (intent) {
            HomeViewIntent.OnInitView -> networkMonitorChecker()
            HomeViewIntent.OnCameraFlash -> handleCameraFlash()
            HomeViewIntent.OnNavigateToCamera -> handleNavigateToCamera()
            HomeViewIntent.OnFailureModalClose -> handleFailureModalClose()
            HomeViewIntent.OnGoogleAuthMemories -> handleGoogleAuthMemories()
            is HomeViewIntent.OnGoogleAuth -> handleGoogleAuth(intent.authType)
            is HomeViewIntent.OnSaveMemory -> handleSaveMemory(intent.saveMemory)
            is HomeViewIntent.OnTakePicture -> handleTakePicture(intent.imageBitmap)
            is HomeViewIntent.OnBackHandler -> handleBackHandler(intent.mainStateType)
            is HomeViewIntent.OnFailureCheckAuth -> handleFailureCheckAuth(intent.cause)
            is HomeViewIntent.OnGoogleAuthMemory -> handleGoogleAuthMemory(intent.saveMemory)
            is HomeViewIntent.OnFailureModalRetry -> handleFailureModalRetry(intent.failureType)
        }
    }

    private fun handleCameraFlash() {
        val flashType = when (viewState.flashType) {
            FlashType.Off -> FlashType.On
            FlashType.On -> FlashType.Auto
            FlashType.Auto -> FlashType.Off
        }
        onState { it.copy(flashType = flashType) }
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
        val encodedImage = imageBitmap.toBase64(quality = 50)
        getDescription(encodedImage)
    }

    private fun getDescription(encodedImage: String) {
        getPromptUseCase(PromptType.ImageDescription)
            .flowOn(dispatcher)
            .onStart { onState { it.getDescriptionStartState() } }
            .flatMapConcat { textPrompt ->
                createContentUseCase {
                    // TODO: In the future, allow users to select the model type from the UI.
                    model(ModelType.GEMINI_1_5_FLASH)
                    image(encodedImage)
                    text(textPrompt)
                }
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

    private fun handleFailureCheckAuth(cause: Throwable) {
        getDescriptionFailure(cause)
    }

    private fun handleGoogleAuthMemories() {
        if (viewState.isOnLine) {
            onEffect { HomeViewEffect.CheckAuthMemories(viewState.ratioType) }
        } else onState {
            it.copy(
                failureType = FailureType.NavigateToMemoriesError,
                shouldShowFailure = true
            )
        }
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

            FailureType.NavigateToMemoriesError -> {
                onState { it.copy(shouldShowFailure = false) }
                handleGoogleAuthMemories()
            }

            else -> Unit
        }
    }

    private fun networkMonitorChecker() {
        watchNetworkStateUseCase()
            .flowOn(dispatcher)
            .catch { onState { it.copy(isOnLine = false) } }
            .onEach { result ->
                onState { it.copy(isOnLine = result.isOnline) }
            }
            .launchIn(viewModelScope)
    }
}
