package com.fappslab.myais.arch.camerax

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaActionSound
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ExperimentalZeroShutterLag
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.fappslab.myais.arch.camerax.model.CameraFlashType
import com.fappslab.myais.arch.camerax.model.RatioType
import com.fappslab.myais.arch.extension.cropToAspectRatio
import com.fappslab.myais.arch.extension.isNotNull
import com.fappslab.myais.arch.extension.orZero
import java.util.concurrent.Executors

internal class CameraXPreviewImpl(
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView
) : CameraXPreview {

    private val context by lazy { previewView.context }

    private var cameraExecutor = Executors.newSingleThreadExecutor()
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var flashMode = ImageCapture.FLASH_MODE_OFF
    private var ratioType = RatioType.RATIO_1_1
    private var isShutterSound = false
    private var quality = 80

    private var imageCaptureUseCase: ImageCapture? = null
    private var camera: Camera? = null

    init {
        initializeCamera()
    }

    private fun initializeCamera() {
        setupCameraUseCases()
    }

    private fun setupCameraUseCases() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            if (previewView.display.isNotNull()) {
                bindCameraUseCases(cameraProvider)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()

        val previewUseCase = createPreviewUseCase()
        val imageAnalysisUseCase = createImageAnalysisUseCase()
        imageCaptureUseCase = createImageCaptureUseCase()

        try {
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                createCameraSelector(),
                previewUseCase,
                imageAnalysisUseCase,
                imageCaptureUseCase
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createPreviewUseCase(): Preview {
        return Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }
    }

    private fun createImageAnalysisUseCase(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(cameraExecutor, FaceAnalyzer()) }
    }

    @OptIn(ExperimentalZeroShutterLag::class)
    private fun createImageCaptureUseCase(): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG)
            .setTargetRotation(previewView.display?.rotation.orZero())
            .setJpegQuality(quality)
            .setFlashMode(flashMode)
            .build()
    }

    private fun createCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
    }

    override fun setAspectRatio(ratioType: RatioType) {
        this.ratioType = ratioType
        restartCamera()
    }

    override fun setShutterSound(isShutterSound: Boolean) {
        this.isShutterSound = isShutterSound
    }

    override fun setPhotoQuality(quality: Int) {
        this.quality = quality
        restartCamera()
    }

    override fun setLinearZoom(value: Float) {
        camera?.cameraControl?.setLinearZoom(value)
    }

    override fun takePicture(onCaptureSuccess: (Bitmap) -> Unit) {
        val callback = object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                playShutterSound()
                val bitmap = image.toTransformedBitmap()
                onCaptureSuccess(bitmap)
                image.close()
            }

            override fun onError(cause: ImageCaptureException) {
                cause.printStackTrace()
            }
        }
        imageCaptureUseCase?.takePicture(cameraExecutor, callback)
    }

    override fun flipCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else CameraSelector.LENS_FACING_BACK
        initializeCamera()
    }

    override fun toggleFlash(cameraFlashType: CameraFlashType) {
        flashMode = cameraFlashType.flashMode
        restartCamera()
    }

    override fun restartCamera() {
        if (cameraExecutor.isShutdown) {
            cameraExecutor = Executors
                .newSingleThreadExecutor()
        }
        setupCameraUseCases()
    }

    override fun shutdownCamera() {
        ProcessCameraProvider
            .getInstance(context)
            .get().unbindAll()
        cameraExecutor.shutdown()
    }

    private fun playShutterSound() {
        if (!isShutterSound) return
        val sound = MediaActionSound()
        sound.play(MediaActionSound.SHUTTER_CLICK)
    }

    private fun ImageProxy.toTransformedBitmap(): Bitmap {
        val bitmap = this.toBitmap()
        val matrix = Matrix()

        applyRotation(matrix, imageInfo.rotationDegrees)
        applyMirroring(matrix)

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        ).cropToAspectRatio(ratioType)
    }

    private fun applyRotation(matrix: Matrix, rotationDegrees: Int) {
        if (rotationDegrees != 0) {
            matrix.postRotate(rotationDegrees.toFloat())
        }
    }

    private fun applyMirroring(matrix: Matrix) {
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            matrix.preScale(-1.0f, 1.0f)
            matrix.postRotate(180f)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private class FaceAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            val image = imageProxy.image
            image?.close()
            imageProxy.close()
        }
    }
}
