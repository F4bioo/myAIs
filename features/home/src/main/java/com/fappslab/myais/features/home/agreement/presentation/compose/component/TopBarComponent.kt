package com.fappslab.myais.features.home.agreement.presentation.compose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.features.home.R
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarComponent(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        navigationIcon = {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.app_name),
                style = PlutoTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopBarComponentPreview() {
    TopBarComponent(

    )
}
