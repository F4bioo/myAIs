package com.fappslab.myais.arch.camerax.model

import androidx.annotation.DrawableRes
import androidx.camera.core.ImageCapture

sealed class FlashType(val flashMode: Int) {
    data class On(@DrawableRes val iconRes: Int) : FlashType(ImageCapture.FLASH_MODE_ON)
    data class Off(@DrawableRes val iconRes: Int) : FlashType(ImageCapture.FLASH_MODE_OFF)
    data class Auto(@DrawableRes val iconRes: Int) : FlashType(ImageCapture.FLASH_MODE_AUTO)
}
