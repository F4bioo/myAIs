package com.fappslab.myais.memories.presentation.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fappslab.myais.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.design.accessibility.semantics
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.memories.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmptyScreenComponent(
    modifier: Modifier = Modifier,
    memories: List<Memory>?,
    onNavigationIconClicked: () -> Unit,
) {
    val composition by rememberLottieComposition(RawRes(R.raw.astronaut))
    val emptyText = stringResource(R.string.memories_found)

    if (memories.isNullOrEmpty()) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.size(PlutoTheme.dimen.dp48),
                            onClick = onNavigationIconClicked,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = PlutoTheme.text.colorPlaceholder,
                                contentDescription = stringResource(R.string.memories_navigate_to_previous_screen),
                            )
                        }
                    },
                    title = {
                        Text(
                            modifier = Modifier.semantics { heading() },
                            text = stringResource(R.string.memories_subtitle),
                            style = PlutoTheme.typography.titleLarge,
                            color = PlutoTheme.text.colorPlaceholder
                        )
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.semantics(
                        mergeDescendants = true
                    ) {
                        this.contentDescription = emptyText
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(PlutoTheme.dimen.dp200),
                        iterations = LottieConstants.IterateForever,
                        composition = composition
                    )
                    Text(
                        modifier = Modifier
                            .clearAndSetSemantics { }
                            .padding(horizontal = PlutoTheme.dimen.dp32)
                            .offset(y = -PlutoTheme.dimen.dp24),
                        text = emptyText,
                        textAlign = TextAlign.Center,
                        style = PlutoTheme.typography.bodyLarge,
                        color = PlutoTheme.text.colorPrimary
                    )
                    Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyScreenComponentPreview() {
    EmptyScreenComponent(
        memories = emptyList(),
        onNavigationIconClicked = {}
    )
}
