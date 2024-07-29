package com.fappslab.myais.features.home.agreement.presentation.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.features.home.R
import com.fappslab.myais.libraries.design.extension.clickable
import com.fappslab.myais.libraries.design.theme.PlutoTheme

internal const val TERMS_OF_SERVICE_URL = "https://fappslab.com/myAIs/terms.html"
internal const val PRIVACY_POLICY_URL = "https://fappslab.com/myAIs/privacy.html"

@Composable
internal fun ConditionsComponent(
    modifier: Modifier = Modifier,
    onClicked: (String) -> Unit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.agreement_continue_text),
            style = PlutoTheme.typography.bodyLarge,
            color = PlutoTheme.text.colorPlaceholder,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
        Text(
            modifier = Modifier
                .clickable(
                    onClickLabel = stringResource(R.string.agreement_desc_open_terms),
                    onClicked = { onClicked(TERMS_OF_SERVICE_URL) }
                ),
            text = stringResource(R.string.agreement_terms),
            style = PlutoTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = PlutoTheme.colors.blueLink,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
        Text(
            modifier = Modifier
                .clickable(
                    onClickLabel = stringResource(R.string.agreement_desc_open_privacy),
                    onClicked = { onClicked(PRIVACY_POLICY_URL) }
                ),
            text = stringResource(R.string.agreement_privacy),
            style = PlutoTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = PlutoTheme.colors.blueLink,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsComponentPreview() {
    ConditionsComponent(
        modifier = Modifier.fillMaxWidth(),
        onClicked = {},
    )
}
