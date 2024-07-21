package com.fappslab.myais.libraries.arch.camerax

import android.graphics.Bitmap
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.fappslab.myais.libraries.arch.camerax.model.CameraFlashType
import com.fappslab.myais.libraries.arch.camerax.model.RatioType

interface CameraXPreview {
    fun setAspectRatio(ratioType: RatioType)
    fun setShutterSound(isShutterSound: Boolean)
    fun setPhotoQuality(@IntRange(from = 1, to = 100) quality: Int = 80)
    fun setLinearZoom(@FloatRange(from = 0.0, to = 1.0) value: Float = 0f)
    fun takePicture(onCaptureSuccess: (Bitmap) -> Unit)
    fun toggleFlash(cameraFlashType: CameraFlashType)
    fun flipCamera()
    fun restartCamera()
    fun shutdownCamera()
}
