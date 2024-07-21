package com.fappslab.myais.features.home.main.presentation.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.libraries.design.accessibility.semantics
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import com.fappslab.myais.features.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarComponent(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    shouldShowNavigationIcon: Boolean,
    shouldShowActionButton: Boolean = true,
    isActionButtonEnabled: Boolean,
    onNavigationIconClicked: () -> Unit,
    onActionButtonClicked: () -> Unit,
) {

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        navigationIcon = {
            if (shouldShowNavigationIcon) {
                IconButton(
                    modifier = Modifier.size(PlutoTheme.dimen.dp48),
                    onClick = onNavigationIconClicked,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = PlutoTheme.text.colorPlaceholder,
                        contentDescription = stringResource(R.string.home_desc_navigate_to_camera_screen),
                    )
                }
            } else Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = stringResource(R.string.app_name),
                    style = PlutoTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            if (shouldShowActionButton) {
                IconButton(
                    modifier = Modifier.size(PlutoTheme.dimen.dp48),
                    enabled = isActionButtonEnabled,
                    onClick = onActionButtonClicked,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cloud_on),
                        tint = PlutoTheme.text.colorPlaceholder,
                        contentDescription = stringResource(R.string.home_desc_navigate_to_memories_screen),
                    )
                }
            } else Spacer(modifier = modifier.size(PlutoTheme.dimen.dp48))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PlutoTopBarComponentPreview() {
    TopBarComponent(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        shouldShowNavigationIcon = true,
        shouldShowActionButton = true,
        isActionButtonEnabled = true,
        onNavigationIconClicked = {},
        onActionButtonClicked = {},
    )
}
