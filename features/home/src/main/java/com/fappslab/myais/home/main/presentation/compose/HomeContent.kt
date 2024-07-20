package com.fappslab.myais.home.main.presentation.compose

import CameraPreviewComponent
import androidx.activity.compose.BackHandler
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.fappslab.myais.home.main.presentation.compose.component.TopBarComponent
import com.fappslab.myais.home.main.presentation.extension.onEyeButtonClicked
import com.fappslab.myais.home.main.presentation.extension.typeOf
import com.fappslab.myais.home.main.presentation.model.FailureType
import com.fappslab.myais.home.main.presentation.model.MainStateType
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewIntent
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewState
import com.fappslab.myais.remote.source.IMAGE_JPEG_MIME_TYPE

private const val FILE_NAME = "myAIs_memory_%d.jpeg"

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    cameraXPreview: CameraXPreview,
    state: HomeViewState,
    intent: (HomeViewIntent) -> Unit,
) {
    var isShutterEffect by remember { mutableStateOf(value = false) }
    val scrimColor = MaterialTheme.colorScheme.surface
        .copy(PlutoTheme.opacity.semiTransparent)
    val description = if (state.mainStateType == MainStateType.Camera) {
        stringResource(R.string.home_simple_description)
    } else state.imageDescription.text

    CameraPreviewComponent(
        modifier = modifier.fillMaxSize(),
        previewView = previewView,
        imageBitmap = state.imageBitmap,
        mainStateType = state.mainStateType,
        isShutterEffect = isShutterEffect,
        onRestartCamera = {
            cameraXPreview.restartCamera()
        },
        onShutdownCamera = {
            isShutterEffect = false
            cameraXPreview.shutdownCamera()
        }
    ) {
        Column(
            modifier = Modifier
        ) {
            TopBarComponent(
                scrimColor = scrimColor,
                state = state,
                intent = intent
            )
            Spacer(modifier = Modifier.weight(1f))
            BodyDescriptionComponent(
                scrimColor = scrimColor,
                description = description,
                state = state,
                intent = intent
            )
            FooterEyeCaptureComponent(
                scrimColor = scrimColor,
                description = description,
                cameraXPreview = cameraXPreview,
                state = state,
                intent = intent
            ) {
                isShutterEffect = it
            }
        }
    }
    PlutoLoadingDialog(
        shouldShowDialog = state.shouldShowLoading,
        shouldShowLabel = true
    )
    ErrorModal(
        shouldShowFailure = state.shouldShowFailure,
        failureType = state.failureType,
        intent = intent
    )
    DisposableEffect(LocalLifecycleOwner.current) {
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

@Composable
private fun ErrorModal(
    shouldShowFailure: Boolean,
    failureType: FailureType,
    intent: (HomeViewIntent) -> Unit
) {

    PlutoModalComponent(
        isSheetSwipeEnabled = false,
        shouldShow = shouldShowFailure,
        titleRes = failureType.titleRes,
        messageRes = failureType.messageRes,
        image = {
            Image(
                modifier = Modifier.size(PlutoTheme.dimen.dp64),
                painter = painterResource(failureType.illuRes),
                colorFilter = ColorFilter.tint(PlutoTheme.colors.stealthGray),
                contentDescription = null
            )
        },
        onDismiss = {
            intent(HomeViewIntent.OnFailureModalClose)
        },
        primaryButton = if (failureType != FailureType.UploadError) {
            {
                buttonTextRes = R.string.common_try_again
                onCLicked = {
                    intent(HomeViewIntent.OnFailureModalRetry(failureType))
                }
            }
        } else null,
        secondaryButton = {
            buttonTextRes = R.string.common_close
            onCLicked = {
                intent(HomeViewIntent.OnFailureModalClose)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarComponent(
    scrimColor: Color,
    state: HomeViewState,
    intent: (HomeViewIntent) -> Unit
) {

    TopBarComponent(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = scrimColor
        ),
        shouldShowNavigationIcon = state.mainStateType == MainStateType.Preview,
        isActionButtonEnabled = state.mainStateType.run {
            this == MainStateType.Camera || this == MainStateType.Preview
        },
        onNavigationIconClicked = { intent(HomeViewIntent.OnNavigateToCamera) },
        onActionButtonClicked = { intent(HomeViewIntent.OnGoogleAuthMemories) }
    )
}

@Composable
private fun BodyDescriptionComponent(
    scrimColor: Color,
    description: String,
    state: HomeViewState,
    intent: (HomeViewIntent) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(scrimColor)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
        BodyDescriptionComponent(
            modifier = Modifier
                .padding(horizontal = PlutoTheme.dimen.dp16)
                .heightIn(min = PlutoTheme.dimen.dp140),
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
    }
}

@Composable
private fun FooterEyeCaptureComponent(
    scrimColor: Color,
    description: String,
    cameraXPreview: CameraXPreview,
    state: HomeViewState,
    intent: (HomeViewIntent) -> Unit,
    onTakePicture: (Boolean) -> Unit
) {
    val stateDescription = when (state.mainStateType) {
        MainStateType.Camera -> stringResource(R.string.home_desc_camera_ready)
        MainStateType.Analyze -> stringResource(R.string.home_desc_describing)
        MainStateType.Preview -> stringResource(R.string.home_desc_preview, description)
    }
    val onEyeButtonClicked: () -> Unit = {
        state.mainStateType.onEyeButtonClicked(
            cameraBlock = {
                cameraXPreview.takePicture {
                    onTakePicture(true)
                    intent(HomeViewIntent.OnTakePicture(it))
                }
            },
            previewBlock = { intent(HomeViewIntent.OnNavigateToCamera) }
        )
    }

    Column(
        modifier = Modifier.background(scrimColor)
    ) {
        FooterEyeCaptureComponent(
            modifier = Modifier.clearAndSetSemantics {
                this.liveRegion = LiveRegionMode.Assertive
                this.stateDescription = stateDescription
                this.role = Role.Button
            },
            mainStateType = state.mainStateType,
            onCameraFlash = { cameraXPreview.toggleFlash(it.typeOf()) },
            onCameraPhoto = onEyeButtonClicked,
            onCameraFlip = { cameraXPreview.flipCamera() }
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
    }
}

@Preview(device = "id:pixel_7")
@Composable
private fun HomeContentPreview() {
    val imageDescription = Description(loremIpsum { 30 })
    val state = HomeViewState(
        imageDescription = imageDescription,
        mainStateType = MainStateType.Camera,
    )
    PlutoTheme(
        darkTheme = true
    ) {
        HomeContent(
            previewView = PreviewView(LocalContext.current),
            cameraXPreview = fakeCameraXPreview(),
            state = state,
            intent = {},
        )
    }
}
