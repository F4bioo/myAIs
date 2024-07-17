package com.fappslab.myais.design.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fappslab.myais.design.R
import com.fappslab.myais.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.design.theme.PlutoTheme

@Composable
fun PlutoLoadingDialog(
    shouldShowDialog: Boolean,
    shouldShowLabel: Boolean,
) {

    if (shouldShowDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                shape = RoundedCornerShape(PlutoTheme.radius.large),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .width(PlutoTheme.dimen.dp200)
                    .wrapContentHeight()
                    .padding(PlutoTheme.dimen.dp16)
            ) {
                Column(
                    modifier = Modifier
                        .clearAndSetSemantics {
                            this.liveRegion = LiveRegionMode.Assertive
                            this.contentDescription = stringResource(
                                R.string.common_desc_loading_alert_message
                            )
                        }
                        .padding(PlutoTheme.dimen.dp16),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    if (shouldShowLabel) {
                        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
                        Text(
                            text = stringResource(R.string.common_wait),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlutoLoadingDialogPreview() {
    PlutoLoadingDialog(
        shouldShowDialog = true,
        shouldShowLabel = true
    )
}
