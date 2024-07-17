package com.fappslab.myais.memories.presentation.compose.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.fappslab.myais.design.components.alert.PlutoAlertDialogComponent
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.memories.R

@Composable
internal fun DeleteAlertDialogComponent(
    shouldShowDialog: Boolean,
    onDismissRequested: () -> Unit,
    onPrimaryClicked: () -> Unit
) {

    PlutoAlertDialogComponent(
        contentDescription = stringResource(R.string.memories_desc_delete_memory_alert_message),
        shouldShowDialog = shouldShowDialog,
        onDismissRequest = onDismissRequested,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                tint = PlutoTheme.text.colorPlaceholder,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.memories_delete_memory),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.memories_delete_memory_alert_message)
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

@Preview(showBackground = true)
@Composable
private fun DeleteAlertDialogComponentPreview() {
    DeleteAlertDialogComponent(
        shouldShowDialog = true,
        onDismissRequested = {},
        onPrimaryClicked = {}
    )
}
