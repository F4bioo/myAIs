package com.fappslab.myais.libraries.design.components.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.libraries.design.R
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.accessibility.semantics
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
fun PlutoDragHandleComponent(
    isSheetSwipeEnabled: Boolean,
    onClosed: () -> Unit = {}
) {
    val modifier = if (isSheetSwipeEnabled) {
        Modifier.clearAndSetSemantics { }
    } else Modifier

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = if (isSheetSwipeEnabled) {
            Alignment.Center
        } else Alignment.CenterEnd
    ) {
        if (isSheetSwipeEnabled) {
            Spacer(
                modifier = Modifier
                    .padding(top = PlutoTheme.dimen.dp16)
                    .size(
                        width = PlutoTheme.dimen.dp48,
                        height = PlutoTheme.dimen.dp4
                    )
                    .background(
                        shape = RoundedCornerShape(PlutoTheme.radius.small),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
            )
        } else IconButton(
            modifier = Modifier
                .semantics {
                    onClick(
                        label = stringResource(R.string.common_close),
                        action = null
                    )
                }
                .padding(
                    top = PlutoTheme.dimen.dp16,
                    end = PlutoTheme.dimen.dp16
                )
                .size(PlutoTheme.dimen.dp24),
            onClick = onClosed
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(R.string.common_close)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DragHandlePreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        verticalArrangement = Arrangement.spacedBy(PlutoTheme.dimen.dp16)
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PlutoDragHandleComponent(
                isSheetSwipeEnabled = true
            )
        }
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PlutoDragHandleComponent(
                isSheetSwipeEnabled = false
            )
        }
    }
}
