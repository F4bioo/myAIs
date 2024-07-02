package com.fappslab.myais.home.agreement.presentation.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.design.components.button.ButtonType
import com.fappslab.myais.design.components.button.PlutoButtonComponent
import com.fappslab.myais.design.theme.PlutoTheme

@Composable
internal fun BodyComponent(
    modifier: Modifier = Modifier,
    isAlwaysDenied: Boolean = false,
    isGrantedPermission: Boolean,
    onRequestClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    Column(
        modifier = modifier
    ) {

        CheckBoxComponent(
            title = "Camera Access",
            description = "Allow myAIs app to access your \ncamera to take photos.",
            isEnable = !isGrantedPermission,
            isChecked = isGrantedPermission,
            onClicked = onRequestClicked
        )
        if (isAlwaysDenied) {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp32))
            PlutoButtonComponent(
                modifier = Modifier
                    .semantics {
                        liveRegion = LiveRegionMode.Assertive
                    }
                    .fillMaxWidth()
                    .padding(horizontal = PlutoTheme.dimen.dp48),
                text = "Do you want to open the app settings to allow the camera?",
                buttonType = ButtonType.Tertiary,
                onClick = onSettingsClicked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BodyComponentPreview() {
    BodyComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PlutoTheme.dimen.dp16),
        isAlwaysDenied = true,
        isGrantedPermission = true,
        onRequestClicked = {},
        onSettingsClicked = {}
    )
}
