package com.fappslab.myais.home.agreement.presentation.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.fappslab.myais.design.extension.clickable
import com.fappslab.myais.design.theme.PlutoTheme

@Composable
internal fun CheckBoxComponent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    isEnable: Boolean,
    isChecked: Boolean,
    onClicked: () -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(value = false) }
    LaunchedEffect(isChecked) {
        checked = isChecked
    }

    Box(
        modifier = modifier
            .clickable(enabled = isEnable) {
                onClicked()
            }
            .fillMaxWidth()
            .padding(PlutoTheme.dimen.dp16)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = PlutoTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
                Text(
                    text = description,
                    style = PlutoTheme.typography.bodyMedium,
                    color = PlutoTheme.text.colorPlaceholder,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CheckBoxComponentPreview() {
    CheckBoxComponent(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        isEnable = true,
        title = LoremIpsum(1).values.first(),
        description = LoremIpsum(6).values.first(),
        isChecked = true,
        onClicked = {}
    )
}
