package com.fappslab.myais.arch.camerax.model

import androidx.camera.core.ImageCapture

enum class CameraFlashType(val flashMode: Int) {
    On(flashMode = ImageCapture.FLASH_MODE_ON),
    Off(flashMode = ImageCapture.FLASH_MODE_OFF),
    Auto(flashMode = ImageCapture.FLASH_MODE_AUTO)
}
