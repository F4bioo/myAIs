package com.fappslab.myais.home.main.presentation.viewmodel

import android.graphics.Bitmap
import androidx.annotation.StringRes
import com.fappslab.myais.arch.camerax.model.RatioType
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.home.R
import com.fappslab.myais.home.main.presentation.model.FailureType
import com.fappslab.myais.home.main.presentation.model.MainStateType

internal data class HomeViewState(
    val imageBitmap: Bitmap? = null,
    val saveMemory: SaveMemory? = null,
    val isButtonEyeEnabled: Boolean = true,
    val shouldShowLoading: Boolean = false,
    val shouldShowFailure: Boolean = false,
    val ratioType: RatioType = RatioType.RATIO_16_9,
    val imageDescription: Description = Description(),
    val failureType: FailureType = FailureType.GenericError,
    val mainStateType: MainStateType = MainStateType.Camera,
    @StringRes val uploadDescription: Int = R.string.home_desc_save_memory,
) {

    fun getDescriptionStartState() = copy(
        mainStateType = MainStateType.Analyze,
        shouldShowFailure = false,
        isButtonEyeEnabled = false,
    )

    fun getDescriptionFailureState(failureType: FailureType) = copy(
        mainStateType = MainStateType.Camera,
        failureType = failureType,
        shouldShowFailure = true,
        isButtonEyeEnabled = true,
    )

    fun getDescriptionSuccessState(imageDescription: Description) = copy(
        uploadDescription = R.string.home_desc_save_memory,
        imageDescription = imageDescription,
        mainStateType = MainStateType.Preview,
        isButtonEyeEnabled = true,
    )

    fun handleSaveMemoryStartState() = copy(
        uploadDescription = R.string.home_desc_save_memory,
        shouldShowLoading = true,
        isButtonEyeEnabled = false,
    )

    fun handleSaveMemoryFailureState(failureType: FailureType) = copy(
        mainStateType = MainStateType.Preview,
        failureType = failureType,
        shouldShowLoading = false,
        shouldShowFailure = true,
        isButtonEyeEnabled = true,
    )

    fun handleSaveMemorySuccessState() = copy(
        uploadDescription = R.string.home_desc_uploaded,
        mainStateType = MainStateType.Preview,
        shouldShowLoading = false,
        isButtonEyeEnabled = true,
    )

    fun handleNavigateToCameraState() = copy(
        mainStateType = MainStateType.Camera,
        isButtonEyeEnabled = true,
    )
}
