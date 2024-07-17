package com.fappslab.myais.memories.presentation.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.fappslab.core.navigation.MemoriesRoute
import com.fappslab.myais.arch.auth.AuthManager
import com.fappslab.myais.arch.downloader.Downloader
import com.fappslab.myais.arch.downloader.Downloader.DownloaderParams
import com.fappslab.myais.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.arch.navigation.extension.LocalNavController
import com.fappslab.myais.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.memories.di.MemoriesModuleLoad
import com.fappslab.myais.memories.presentation.compose.component.DeleteAlertDialogComponent
import com.fappslab.myais.memories.presentation.compose.component.DownloadAlertDialogComponent
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewEffect
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewIntent
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
internal fun MemoriesScreen(
    modifier: Modifier = Modifier,
    args: MemoriesRoute
) {
    KoinLazyModuleInitializer(MemoriesModuleLoad)
    val viewModel: MemoriesViewModel = koinViewModel { parametersOf(args) }
    val state by viewModel.state.collectAsState()
    MemoriesEffectObserve(viewModel)

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        MemoriesContent(
            modifier = Modifier.consumeWindowInsets(
                WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
            ),
            contentPadding = it,
            state = state,
            intent = viewModel::onViewIntent
        )
    }
}

@Composable
internal fun MemoriesEffectObserve(
    viewModel: MemoriesViewModel,
    downloader: Downloader = koinInject(),
    authManager: AuthManager = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val shouldShowDownloadDialog = remember { mutableStateOf(value = false) }
    val shouldShowDeleteDialog = remember { mutableStateOf(value = false) }
    val memory = remember { mutableStateOf<Memory?>(value = null) }
    val fileId = remember { mutableStateOf<String?>(value = null) }
    val onPopBackStack: () -> Unit = {
        navController.popBackStack<MemoriesRoute>(inclusive = true)
    }

    viewModel.effect.observeAsEvents { effect ->
        when (effect) {
            MemoriesViewEffect.NavigateToHome -> onPopBackStack()

            MemoriesViewEffect.Logout -> {
                scope.launch {
                    authManager.logout(
                        onSuccess = onPopBackStack,
                        onFailure = {
                            println("<L> onFailure: ${it.message}")
                        }
                    )
                }
            }

            is MemoriesViewEffect.NavigateToDownload -> {
                shouldShowDownloadDialog.value = true
                memory.value = effect.memory
            }

            is MemoriesViewEffect.ShowDeleteDialog -> {
                shouldShowDeleteDialog.value = true
                fileId.value = effect.fileId
            }
        }
    }

    DownloadAlertDialogComponent(
        shouldShowDialog = shouldShowDownloadDialog.value,
        onDismissRequested = {
            shouldShowDownloadDialog.value = false
        },
        onPrimaryClicked = {
            shouldShowDownloadDialog.value = false
            memory.value?.let(downloader::handleDownloadFile)
        }
    )

    DeleteAlertDialogComponent(
        shouldShowDialog = shouldShowDeleteDialog.value,
        onDismissRequested = {
            shouldShowDeleteDialog.value = false
        },
        onPrimaryClicked = {
            shouldShowDeleteDialog.value = false
            fileId.value?.let { viewModel.onViewIntent(MemoriesViewIntent.OnDeleteMemory(it)) }
        }
    )
}

private fun Downloader.handleDownloadFile(memory: Memory) {
    val params = DownloaderParams(
        fileUrl = memory.thumbnailLink,
        fileName = memory.fileName,
        mimeType = memory.mimeType
    )
    downloadFile(params)
}
