package com.fappslab.myais.home.main.presentation.compose

import CameraPreviewComponent
import androidx.activity.compose.BackHandler
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fappslab.myais.arch.camerax.CameraXPreview
import com.fappslab.myais.arch.camerax.compose.fakeCameraXPreview
import com.fappslab.myais.arch.extension.toFile
import com.fappslab.myais.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.design.components.loading.PlutoLoadingDialog
import com.fappslab.myais.design.components.lorem.loremIpsum
import com.fappslab.myais.design.components.modal.PlutoModalComponent
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.home.R
import com.fappslab.myais.home.main.presentation.compose.component.BodyDescriptionComponent
import com.fappslab.myais.home.main.presentation.compose.component.FooterEyeCaptureComponent
import com.fappslab.myais.home.main.presentation.extension.onEyeButtonClicked
import com.fappslab.myais.home.main.presentation.model.FailureType
import com.fappslab.myais.home.main.presentation.model.MainStateType
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewIntent
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewState
import com.fappslab.myais.remote.source.IMAGE_JPEG_MIME_TYPE

private const val FILE_NAME = "myAIs_memory_%d.jpeg"

@Composable
internal fun HomeContent(
    paddingValues: PaddingValues,
    previewView: PreviewView,
    cameraXPreview: CameraXPreview,
    state: HomeViewState,
    intent: (HomeViewIntent) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isShutterEffect by remember { mutableStateOf(false) }
    val onEyeButtonClicked: () -> Unit = {
        state.mainStateType.onEyeButtonClicked(
            cameraBlock = {
                cameraXPreview.takePicture {
                    isShutterEffect = true
                    intent(HomeViewIntent.OnTakePicture(it))
                }
            },
            previewBlock = {
                intent(HomeViewIntent.OnNavigateToCamera)
            }
        )
    }
    val description = if (state.mainStateType == MainStateType.Camera) {
        stringResource(R.string.home_simple_description)
    } else state.imageDescription.text
    val stateDescription = when (state.mainStateType) {
        MainStateType.Camera -> stringResource(R.string.home_desc_camera_ready)
        MainStateType.Analyze -> stringResource(R.string.home_desc_describing)
        MainStateType.Preview -> stringResource(R.string.home_desc_preview, description)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            CameraPreviewComponent(
                modifier = Modifier.padding(horizontal = PlutoTheme.dimen.dp16),
                previewView = previewView,
                imageBitmap = state.imageBitmap,
                ratioType = state.ratioType,
                mainStateType = state.mainStateType,
                isShutterEffect = isShutterEffect,
                onRestartCamera = {
                    cameraXPreview.restartCamera()
                },
                onShutdownCamera = {
                    isShutterEffect = false
                    cameraXPreview.shutdownCamera()
                }
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            BodyDescriptionComponent(
                modifier = Modifier.padding(horizontal = PlutoTheme.dimen.dp16),
                description = description,
                mainStateType = state.mainStateType,
                uploadDescription = state.uploadDescription,
                onUploadClicked = {
                    val file = state.imageBitmap?.toFile(
                        fileName = FILE_NAME.format(System.currentTimeMillis()),
                        context = context
                    )
                    val saveMemory = SaveMemory(
                        description = state.imageDescription.text,
                        mimeType = IMAGE_JPEG_MIME_TYPE,
                        fileImage = file,
                    )
                    intent(HomeViewIntent.OnGoogleAuthMemory(saveMemory))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            FooterEyeCaptureComponent(
                modifier = Modifier.clearAndSetSemantics {
                    this.liveRegion = LiveRegionMode.Assertive
                    this.stateDescription = stateDescription
                    this.role = Role.Button
                },
                mainStateType = state.mainStateType,
                isButtonEyeEnabled = state.isButtonEyeEnabled,
                onEyeButtonClicked = onEyeButtonClicked
            )
        }
        PlutoLoadingDialog(
            shouldShowDialog = state.shouldShowLoading,
            shouldShowLabel = true
        )
        PlutoModalComponent(
            isDraggable = false,
            shouldShow = state.shouldShowFailure,
            titleRes = state.failureType.titleRes,
            messageRes = state.failureType.messageRes,
            image = {
                Image(
                    modifier = Modifier.size(PlutoTheme.dimen.dp64),
                    painter = painterResource(state.failureType.illuRes),
                    colorFilter = ColorFilter.tint(PlutoTheme.colors.stealthGray),
                    contentDescription = null
                )
            },
            primaryButton = takeIf { state.failureType != FailureType.UploadError }?.let {
                {
                    buttonTextRes = R.string.common_try_again
                    onCLicked = {
                        intent(HomeViewIntent.OnFailureModalRetry(state.failureType))
                    }
                }
            },
            secondaryButton = {
                buttonTextRes = R.string.common_close
                onCLicked = {
                    intent(HomeViewIntent.OnFailureModalClose)
                }
            },
            onDismiss = {
                intent(HomeViewIntent.OnFailureModalClose)
            }
        )
    }
    DisposableEffect(lifecycleOwner) {
        onDispose {
            cameraXPreview.shutdownCamera()
        }
    }
    BackHandler(
        enabled = state.mainStateType != MainStateType.Camera
    ) {
        intent(HomeViewIntent.OnBackHandler(state.mainStateType))
    }
}

@Preview(showSystemUi = true, device = "id:pixel_7")
@Composable
private fun HomeContentPreview() {
    val imageDescription = Description(loremIpsum { 30 })
    val state = HomeViewState(
        imageDescription = imageDescription,
        mainStateType = MainStateType.Camera,
    )
    HomeContent(
        paddingValues = PaddingValues(),
        previewView = PreviewView(LocalContext.current),
        cameraXPreview = fakeCameraXPreview(),
        state = state,
        intent = {},
    )
}
