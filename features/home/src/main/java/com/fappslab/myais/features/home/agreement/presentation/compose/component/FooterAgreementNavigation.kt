package com.fappslab.myais.features.home.agreement.presentation.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.features.home.R
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.components.button.ButtonType
import com.fappslab.myais.libraries.design.components.button.PlutoButtonComponent
import com.fappslab.myais.libraries.design.components.button.model.ButtonState
import com.fappslab.myais.libraries.design.components.footer.PlutoFooterLayout
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
internal fun FooterAgreementNavigationComponent(
    modifier: Modifier = Modifier,
    buttonState: ButtonState,
    onConditionsClicked: (String) -> Unit,
    onContinueClicked: () -> Unit,
) {
    val navigationBarHeight = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        PlutoFooterLayout {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            ConditionsComponent(
                modifier = Modifier.fillMaxWidth(),
                onClicked = { onConditionsClicked(it) }
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            PlutoButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics {
                        val contextDescriptionRes =
                            if (buttonState == ButtonState.Disabled) {
                                R.string.agreement_desc_button_disabled_description
                            } else R.string.agreement_desc_button_enabled_description
                        this.liveRegion = LiveRegionMode.Polite
                        this.stateDescription = stringResource(contextDescriptionRes)
                    },
                text = stringResource(R.string.common_continue),
                buttonType = ButtonType.Primary,
                buttonState = buttonState,
                onClick = onContinueClicked
            )
            Spacer(modifier = Modifier.height(navigationBarHeight))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FooterAgreementNavigationPreview() {
    PlutoTheme(
        darkTheme = false
    ) {
        FooterAgreementNavigationComponent(
            buttonState = ButtonState.Enabled,
            onConditionsClicked = {},
            onContinueClicked = {}
        )
    }
}
