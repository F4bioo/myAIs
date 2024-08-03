package com.fappslab.myais.libraries.arch.camerax.compose

import android.graphics.Bitmap
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ImplementationMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fappslab.myais.libraries.arch.camerax.CameraXPreview
import com.fappslab.myais.libraries.arch.camerax.CameraXPreviewImpl
import com.fappslab.myais.libraries.arch.camerax.model.CameraFlashType
import com.fappslab.myais.libraries.arch.camerax.model.RatioType

@Composable
fun CameraXPreviewProvider(
    content: @Composable () -> Unit
) {
    val previewView = rememberPreviewView()
    val cameraXPreview = rememberCameraXPreview(previewView)

    CompositionLocalProvider(
        LocalPreviewView provides previewView,
        LocalCameraXPreview provides cameraXPreview
    ) {
        content()
    }
}

val LocalPreviewView = compositionLocalOf<PreviewView> {
    error("No PreviewView provided")
}

val LocalCameraXPreview = compositionLocalOf<CameraXPreview> {
    error("No CameraXPreview provided")
}

@Composable
private fun rememberPreviewView(): PreviewView {
    val context = LocalContext.current
    return remember {
        PreviewView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            implementationMode = ImplementationMode.PERFORMANCE
            scaleType = PreviewView.ScaleType.FILL_CENTER
            controller = LifecycleCameraController(context)
        }
    }
}

@Composable
private fun rememberCameraXPreview(
    previewView: PreviewView,
): CameraXPreview {
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember {
        CameraXPreviewImpl(lifecycleOwner, previewView)
    }
}

fun fakeCameraXPreview(): CameraXPreview {
    return object : CameraXPreview {
        override fun setShutterSound(isShutterSound: Boolean) {}
        override fun setAspectRatio(ratioType: RatioType) {}
        override fun setPhotoQuality(quality: Int) {}
        override fun setLinearZoom(value: Float) {}
        override fun takePicture(onCaptureSuccess: (Bitmap) -> Unit) {}
        override fun toggleFlash(cameraFlashType: CameraFlashType) {}
        override fun flipCamera() {}
        override fun restartCamera() {}
        override fun shutdownCamera() {}
    }
}
