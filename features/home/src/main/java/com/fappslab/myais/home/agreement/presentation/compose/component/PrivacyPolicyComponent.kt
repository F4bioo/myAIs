package com.fappslab.myais.home.agreement.presentation.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.design.extension.clickable
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.R

@Composable
internal fun PrivacyPolicyComponent(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {

    Box(
        modifier = modifier.clickable(
            onClickLabel = stringResource(R.string.agreement_desc_open_terms),
            onClicked = onClicked
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(PlutoTheme.dimen.dp16),
            text = buildAnnotatedString {
                append(stringResource(R.string.agreement_continue_text))
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(stringResource(R.string.agreement_terms_text))
                }
            },
            style = PlutoTheme.typography.bodyMedium,
            color = PlutoTheme.text.colorPlaceholder,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsComponentPreview() {
    PrivacyPolicyComponent(
        modifier = Modifier.fillMaxWidth(),
        onClicked = {}
    )
}
