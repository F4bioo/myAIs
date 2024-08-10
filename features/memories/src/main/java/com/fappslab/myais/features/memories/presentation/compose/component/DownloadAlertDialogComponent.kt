package com.fappslab.myais.features.memories.presentation.compose.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.fappslab.myais.libraries.design.components.alert.PlutoAlertDialogComponent
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import com.fappslab.myais.features.memories.R

@Composable
internal fun DownloadAlertDialogComponent(
    shouldShowDialog: Boolean,
    onDismissRequested: () -> Unit,
    onPrimaryClicked: () -> Unit
) {

    PlutoAlertDialogComponent(
        contentDescription = stringResource(R.string.memories_desc_download_memory_alert_message),
        shouldShowDialog = shouldShowDialog,
        onDismissRequest = onDismissRequested,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                tint = PlutoTheme.text.colorPlaceholder,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.memories_download_memory_alert_title),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.memories_download_memory_alert_message),
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequested
            ) {
                Text(
                    text = stringResource(R.string.common_cancel)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onPrimaryClicked
            ) {
                Text(
                    text = stringResource(R.string.common_confirm)
                )
            }
        }
    )
}
