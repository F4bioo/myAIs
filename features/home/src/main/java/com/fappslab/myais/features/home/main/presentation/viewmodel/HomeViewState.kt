package com.fappslab.myais.features.home.main.presentation.viewmodel

import android.graphics.Bitmap
import androidx.annotation.StringRes
import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.model.SaveMemory
import com.fappslab.myais.features.home.R
import com.fappslab.myais.features.home.main.presentation.model.AuthType
import com.fappslab.myais.features.home.main.presentation.model.FailureType
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.MainStateType
import com.fappslab.myais.libraries.arch.camerax.model.RatioType

internal data class HomeViewState(
    val flashType: FlashType,
    val imageBitmap: Bitmap? = null,
    val saveMemory: SaveMemory? = null,
    val shouldShowLoading: Boolean = false,
    val shouldShowFailure: Boolean = false,
    val authType: AuthType = AuthType.Nothing,
    val ratioType: RatioType = RatioType.RATIO_16_9,
    val imageDescription: Description = Description(),
    val failureType: FailureType = FailureType.GenericErrorDescription,
    val mainStateType: MainStateType = MainStateType.Camera,
    @StringRes val uploadDescription: Int = R.string.home_desc_save_memory,
) {

    fun getDescriptionStartState() = copy(
        mainStateType = MainStateType.Analyze,
        shouldShowFailure = false,
    )

    fun getDescriptionFailureState(failureType: FailureType) = copy(
        mainStateType = MainStateType.Camera,
        failureType = failureType,
        shouldShowFailure = true,
    )

    fun getDescriptionSuccessState(imageDescription: Description) = copy(
        uploadDescription = R.string.home_desc_save_memory,
        imageDescription = imageDescription,
        mainStateType = MainStateType.Preview,
    )

    fun handleSaveMemoryStartState() = copy(
        uploadDescription = R.string.home_desc_save_memory,
        shouldShowLoading = true,
    )

    fun handleSaveMemoryFailureState(failureType: FailureType) = copy(
        mainStateType = MainStateType.Preview,
        failureType = failureType,
        shouldShowLoading = false,
        shouldShowFailure = true,
    )

    fun handleSaveMemorySuccessState() = copy(
        uploadDescription = R.string.home_desc_uploaded,
        mainStateType = MainStateType.Preview,
        shouldShowLoading = false,
    )

    fun handleNavigateToCameraState() = copy(
        mainStateType = MainStateType.Camera,
    )

    fun googleAuthResulCanceledState(failureType: FailureType) = copy(
        failureType = failureType,
        shouldShowFailure = true
    )
}
