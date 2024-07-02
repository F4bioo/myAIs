package com.fappslab.myais.home.agreement.presentation.compose

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.arch.simplepermission.extension.rememberPermissionLauncher
import com.fappslab.myais.design.components.button.ButtonType
import com.fappslab.myais.design.components.button.PlutoButtonComponent
import com.fappslab.myais.design.components.footer.PlutoFooterLayout
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.agreement.presentation.compose.component.BodyComponent
import com.fappslab.myais.home.agreement.presentation.compose.component.HeaderComponent
import com.fappslab.myais.home.agreement.presentation.compose.component.PrivacyPolicyComponent
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewIntent
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewState

@Composable
internal fun AgreementContent(
    paddingValues: PaddingValues,
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
    Column(
        modifier = Modifier.padding(paddingValues)
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
                text = "Discover the world with new eyes, use AI to save and relive your visual memories.",
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
        PlutoFooterLayout {
            PrivacyPolicyComponent(
                modifier = Modifier.fillMaxWidth(),
                onClicked = { intent(AgreementViewIntent.OnPrivacyPolicy) }
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue",
                buttonType = ButtonType.Primary,
            )
        }
    }
}

@Preview(showSystemUi = true, device = "id:pixel_4")
@Composable
private fun AgreementContentPreview() {
    AgreementContent(
        paddingValues = PaddingValues(),
        state = AgreementViewState(),
        intent = {}
    )
}
