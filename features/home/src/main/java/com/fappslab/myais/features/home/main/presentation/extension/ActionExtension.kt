package com.fappslab.myais.features.home.main.presentation.extension

import com.fappslab.myais.features.home.main.presentation.model.MainStateType

internal fun MainStateType.onEyeButtonClicked(
    cameraBlock: () -> Unit,
    previewBlock: () -> Unit,
) {
    when (this) {
        MainStateType.Camera -> cameraBlock()
        MainStateType.Preview -> previewBlock()
        else -> Unit
    }
}
