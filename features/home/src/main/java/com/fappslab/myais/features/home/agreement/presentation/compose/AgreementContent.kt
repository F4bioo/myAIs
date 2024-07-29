package com.fappslab.myais.features.home.agreement.presentation.compose

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.features.home.R
import com.fappslab.myais.features.home.agreement.presentation.compose.component.BodyComponent
import com.fappslab.myais.features.home.agreement.presentation.compose.component.ConditionsComponent
import com.fappslab.myais.features.home.agreement.presentation.compose.component.HeaderComponent
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewIntent
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewState
import com.fappslab.myais.libraries.arch.simplepermission.extension.rememberPermissionLauncher
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.components.button.ButtonType
import com.fappslab.myais.libraries.design.components.button.PlutoButtonComponent
import com.fappslab.myais.libraries.design.components.button.model.ButtonState
import com.fappslab.myais.libraries.design.components.footer.PlutoFooterLayout
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
internal fun AgreementContent(
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxSize()
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
        PlutoFooterLayout {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            ConditionsComponent(
                modifier = Modifier.fillMaxWidth(),
                onClicked = { intent(AgreementViewIntent.OnConditions(link = it)) }
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            PlutoButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics {
                        val contextDescriptionRes = if (state.buttonState == ButtonState.Disabled) {
                            R.string.agreement_desc_button_disabled_description
                        } else R.string.agreement_desc_button_enabled_description
                        this.liveRegion = LiveRegionMode.Polite
                        this.stateDescription = stringResource(contextDescriptionRes)
                    },
                text = stringResource(R.string.common_continue),
                buttonType = ButtonType.Primary,
                buttonState = state.buttonState,
                onClick = { intent(AgreementViewIntent.OnContinue) }
            )
        }
    }
}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
private fun AgreementContentPreview() {
    PlutoTheme(
        darkTheme = false
    ) {
        AgreementContent(
            state = AgreementViewState(
                isGrantedPermission = false,
                isAlwaysDenied = false,
                buttonState = ButtonState.Enabled
            ),
            intent = {}
        )
    }
}
