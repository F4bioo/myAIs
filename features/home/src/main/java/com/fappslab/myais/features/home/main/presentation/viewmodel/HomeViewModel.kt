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
import com.fappslab.myais.features.home.main.presentation.model.AuthType
import com.fappslab.myais.features.home.main.presentation.model.FailureType
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.MainStateType
import com.fappslab.myais.libraries.arch.extension.toBase64
import com.fappslab.myais.libraries.arch.viewmodel.ViewIntent
import com.fappslab.myais.libraries.arch.viewmodel.ViewModel
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
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
    flashType: FlashType = FlashType.Off,
    private val getPromptUseCase: GetPromptUseCase,
    private val createContentUseCase: CreateContentUseCase,
    private val uploadDriveFileUseCase: UploadDriveFileUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<HomeViewState, HomeViewEffect>(HomeViewState(flashType)),
    ViewIntent<HomeViewIntent> {

    override fun onViewIntent(intent: HomeViewIntent) {
        when (intent) {
            HomeViewIntent.OnCameraFlash -> handleCameraFlash()
            HomeViewIntent.OnNavigateToCamera -> handleNavigateToCamera()
            HomeViewIntent.OnGoogleAuthResulOk -> handleGoogleAuthResulOk()
            HomeViewIntent.OnFailureModalClose -> handleFailureModalClose()
            HomeViewIntent.OnMemoriesAuthChecker -> handleMemoriesAuthChecker()
            is HomeViewIntent.OnSaveMemory -> handleSaveMemory(intent.saveMemory)
            is HomeViewIntent.OnTakePicture -> handleTakePicture(intent.imageBitmap)
            is HomeViewIntent.OnBackHandler -> handleBackHandler(intent.mainStateType)
            is HomeViewIntent.OnFailureCheckAuth -> handleFailureCheckAuth(intent.cause)
            is HomeViewIntent.OnUploadAuthChecker -> handleUploadAuthChecker(intent.saveMemory)
            is HomeViewIntent.OnFailureModalRetry -> handleFailureModalRetry(intent.failureType)
            is HomeViewIntent.OnGoogleAuthResulCanceled -> handleGoogleAuthResulCanceled(intent.status)
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
        onState { it.handleSaveMemoryFailureState(FailureType.UploadErrorMemory) }
        cause.printStackTrace()
    }

    private fun handleNavigateToCamera() {
        onState { it.handleNavigateToCameraState() }
    }

    private fun handleTakePicture(imageBitmap: Bitmap) {
        onState { it.copy(imageBitmap = imageBitmap) }
        getDescription(imageBitmap.toBase64(quality = 50))
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
        cause.printStackTrace()
        val failureType = when (cause) {
            is InternetThrowable -> FailureType.ConnectionErrorDescription
            is HttpThrowable -> FailureType.AnalyzeErrorDescription
            else -> FailureType.GenericErrorDescription
        }
        onState { it.getDescriptionFailureState(failureType) }
    }

    private fun getDescriptionSuccess(imageDescription: Description) {
        onState { it.getDescriptionSuccessState(imageDescription) }
    }

    private fun handleBackHandler(mainStateType: MainStateType) {
        if (mainStateType == MainStateType.Preview) handleNavigateToCamera()
    }

    private fun handleFailureCheckAuth(cause: Throwable) {
        getDescriptionFailure(cause)
    }

    private fun handleMemoriesAuthChecker() {
        onState { it.copy(shouldShowFailure = false, authType = AuthType.NavigateToMemories) }
        onEffect { HomeViewEffect.MemoriesAuthManager(viewState.ratioType) }
    }

    private fun handleUploadAuthChecker(saveMemory: SaveMemory) {
        onState { it.copy(saveMemory = saveMemory, authType = AuthType.UploadMemory) }
        onEffect { HomeViewEffect.UploadAuthManager(saveMemory) }
    }

    private fun handleFailureModalRetry(failureType: FailureType) {
        when (failureType) {
            FailureType.GenericErrorDescription,
            FailureType.ConnectionErrorDescription,
            FailureType.AnalyzeErrorDescription -> {
                viewState.imageBitmap?.let { imageBitmap ->
                    getDescription(encodedImage = imageBitmap.toBase64())
                }
            }

            FailureType.ConnectionErrorMemories -> handleMemoriesAuthChecker()
            else -> Unit
        }
    }

    private fun handleGoogleAuthResulOk() {
        when (viewState.authType) {
            AuthType.NavigateToMemories -> {
                onEffect { HomeViewEffect.NavigateToMemories(viewState.ratioType) }
            }

            AuthType.UploadMemory -> viewState.saveMemory?.let(::handleSaveMemory)
            else -> Unit
        }
    }

    private fun handleGoogleAuthResulCanceled(status: Status) {
        when (status.statusCode) {
            CommonStatusCodes.NETWORK_ERROR -> {
                onState { it.googleAuthResulCanceledState(FailureType.ConnectionErrorMemories) }
            }

            else -> Unit
        }
    }
}
