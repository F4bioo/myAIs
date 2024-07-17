package com.fappslab.myais.design.components.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fappslab.myais.design.theme.PlutoTheme

@Composable
fun PlutoSnackBarComponent(
    snackBarHostState: SnackbarHostState,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PlutoTheme.dimen.dp4),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = snackBarHostState)
    }
}

@Composable
fun rememberSnackBarHostState(): SnackbarHostState {
    return remember { SnackbarHostState() }
}
