package com.fappslab.myais.features.home.main.presentation.viewmodel

import android.graphics.Bitmap
import com.fappslab.myais.core.domain.model.SaveMemory
import com.fappslab.myais.features.home.main.presentation.model.FailureType
import com.fappslab.myais.features.home.main.presentation.model.MainStateType
import com.google.android.gms.common.api.Status

internal sealed interface HomeViewIntent {
    data object OnCameraFlash : HomeViewIntent
    data object OnNavigateToCamera : HomeViewIntent
    data object OnGoogleAuthResulOk : HomeViewIntent
    data object OnFailureModalClose : HomeViewIntent
    data object OnMemoriesAuthChecker : HomeViewIntent
    data class OnGoogleAuthResulCanceled(val status: Status) : HomeViewIntent
    data class OnUploadAuthChecker(val saveMemory: SaveMemory) : HomeViewIntent
    data class OnFailureCheckAuth(val cause: Throwable) : HomeViewIntent
    data class OnTakePicture(val imageBitmap: Bitmap) : HomeViewIntent
    data class OnSaveMemory(val saveMemory: SaveMemory) : HomeViewIntent
    data class OnBackHandler(val mainStateType: MainStateType) : HomeViewIntent
    data class OnFailureModalRetry(val failureType: FailureType) : HomeViewIntent
}
