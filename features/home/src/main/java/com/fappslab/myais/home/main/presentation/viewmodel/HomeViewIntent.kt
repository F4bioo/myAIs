package com.fappslab.myais.home.main.presentation.viewmodel

import android.graphics.Bitmap
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.home.main.presentation.model.AuthType
import com.fappslab.myais.home.main.presentation.model.FailureType
import com.fappslab.myais.home.main.presentation.model.MainStateType

internal sealed interface HomeViewIntent {
    data object OnNavigateToCamera : HomeViewIntent
    data object OnFailureModalClose : HomeViewIntent
    data object OnGoogleAuthMemories : HomeViewIntent
    data class OnGoogleAuthMemory(val saveMemory: SaveMemory) : HomeViewIntent
    data class OnGoogleAuth(val authType: AuthType) : HomeViewIntent
    data class OnTakePicture(val imageBitmap: Bitmap) : HomeViewIntent
    data class OnSaveMemory(val saveMemory: SaveMemory) : HomeViewIntent
    data class OnBackHandler(val mainStateType: MainStateType) : HomeViewIntent
    data class OnFailureModalRetry(val failureType: FailureType) : HomeViewIntent
}
