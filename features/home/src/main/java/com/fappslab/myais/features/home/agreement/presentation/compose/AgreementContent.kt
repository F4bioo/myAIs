package com.fappslab.myais.features.home.agreement.presentation.compose

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.fappslab.myais.features.home.R
import com.fappslab.myais.features.home.agreement.presentation.compose.component.BodyComponent
import com.fappslab.myais.features.home.agreement.presentation.compose.component.FooterAgreementNavigationComponent
import com.fappslab.myais.features.home.agreement.presentation.compose.component.HeaderComponent
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewIntent
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewState
import com.fappslab.myais.libraries.arch.camerax.CameraXPreview
import com.fappslab.myais.libraries.arch.camerax.compose.fakeCameraXPreview
import com.fappslab.myais.libraries.arch.simplepermission.extension.rememberPermissionLauncher
import com.fappslab.myais.libraries.design.components.button.model.ButtonState
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
internal fun AgreementContent(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    cameraXPreview: CameraXPreview,
    state: AgreementViewState,
    intent: (AgreementViewIntent) -> Unit,
) {
    val permissionLauncher = rememberPermissionLauncher(Manifest.permission.CAMERA) { result ->
        intent(AgreementViewIntent.OnPermissionResult(result))
    }
    val currentPermissionStatus = remember { permissionLauncher.currentPermissionStatus() }

    LaunchedEffect(permissionLauncher) {
        intent(AgreementViewIntent.OnPermissionResult(currentPermissionStatus))
    }
    CameraPreviewHandler(
        modifier = modifier,
        previewView = previewView,
        onRestartCamera = { cameraXPreview.restartCamera() },
        isGrantedCameraPermission = state.isGrantedPermission
    ) {
        Column(
            modifier = Modifier
                .padding(PlutoTheme.dimen.dp16)
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            HeaderComponent(
                modifier = Modifier.fillMaxWidth(),
                isGrantedPermission = state.isGrantedPermission,
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
            Text(
                modifier = Modifier.padding(horizontal = PlutoTheme.dimen.dp8),
                text = stringResource(R.string.agreement_brand_message),
                textAlign = TextAlign.Center,
                style = PlutoTheme.typography.titleLarge.copy(fontStyle = FontStyle.Italic),
                color = PlutoTheme.text.colorPrimary
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                BodyComponent(
                    isAlwaysDenied = state.isAlwaysDenied,
                    isGrantedPermission = state.isGrantedPermission,
                    onRequestClicked = { permissionLauncher.requestPermission() },
                    onSettingsClicked = { intent(AgreementViewIntent.OnOpenSettings) }
                )
            }
        }
        FooterAgreementNavigationComponent(
            modifier = Modifier.background(Color.Transparent),
            buttonState = state.buttonState,
            onConditionsClicked = { intent(AgreementViewIntent.OnConditions(link = it)) },
            onContinueClicked = { intent(AgreementViewIntent.OnContinue) }
        )
    }
}

@Composable
private fun CameraPreviewHandler(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    onRestartCamera: () -> Unit,
    isGrantedCameraPermission: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrimColor = MaterialTheme.colorScheme.surface
        .copy(PlutoTheme.opacity.semiTransparent)

    if (isGrantedCameraPermission) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            AndroidView(
                modifier = Modifier.matchParentSize(),
                factory = {
                    onRestartCamera()
                    previewView
                }
            )
            Column(
                modifier = Modifier.background(scrimColor),
                content = content
            )
        }

    } else Column(content = content)
}


@Preview(device = "id:pixel_7", showBackground = true)
@Composable
private fun AgreementContentPreview() {
    val state = AgreementViewState(
        isGrantedPermission = false,
        isAlwaysDenied = false,
        buttonState = ButtonState.Enabled
    )
    PlutoTheme(
        darkTheme = false
    ) {
        AgreementContent(
            previewView = PreviewView(LocalContext.current),
            cameraXPreview = fakeCameraXPreview(),
            state = state,
            intent = {}
        )
    }
}
