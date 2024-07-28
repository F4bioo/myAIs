package com.fappslab.myais.features.home.main.presentation.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.fappslab.myais.core.navigation.MemoriesNavigation
import com.fappslab.myais.core.navigation.MemoriesRoute
import com.fappslab.myais.features.home.di.HomeModuleLoad
import com.fappslab.myais.features.home.main.presentation.extension.getAuthorizationClient
import com.fappslab.myais.features.home.main.presentation.extension.handleLauncherResult
import com.fappslab.myais.features.home.main.presentation.viewmodel.HomeViewEffect
import com.fappslab.myais.features.home.main.presentation.viewmodel.HomeViewIntent
import com.fappslab.myais.features.home.main.presentation.viewmodel.HomeViewModel
import com.fappslab.myais.libraries.arch.auth.AuthManager
import com.fappslab.myais.libraries.arch.camerax.compose.CameraXPreviewProvider
import com.fappslab.myais.libraries.arch.camerax.compose.LocalCameraXPreview
import com.fappslab.myais.libraries.arch.camerax.compose.LocalPreviewView
import com.fappslab.myais.libraries.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.libraries.arch.navigation.extension.LocalNavController
import com.fappslab.myais.libraries.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun HomeScreen() {
    KoinLazyModuleInitializer(HomeModuleLoad)
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    HomeEffectObserve(viewModel)

    PlutoTheme(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent
    ) {
        CameraXPreviewProvider {
            HomeContent(
                state = state,
                intent = viewModel::onViewIntent,
                previewView = LocalPreviewView.current,
                cameraXPreview = LocalCameraXPreview.current,
            )
        }
    }
}

@Composable
private fun HomeEffectObserve(
    viewModel: HomeViewModel,
    authManager: AuthManager = koinInject(),
    memoriesNavigation: MemoriesNavigation = koinInject()
) {
    val navController = LocalNavController.current
    val signInLauncher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        result.handleLauncherResult(viewModel::onViewIntent)
    }

    viewModel.effect.observeAsEvents { effect ->
        when (effect) {
            is HomeViewEffect.MemoriesAuthManager -> {
                authManager.getAuthorizationClient(
                    onLoggedOut = signInLauncher::launch,
                    onLoggedIn = {
                        val route = MemoriesRoute(effect.ratioType.toRatio())
                        memoriesNavigation.navigateToFeature(navController, route)
                    },
                    onFailure = {
                        viewModel.onViewIntent(HomeViewIntent.OnFailureCheckAuth(cause = it))
                    }
                )
            }

            is HomeViewEffect.UploadAuthManager -> {
                authManager.getAuthorizationClient(
                    onLoggedOut = signInLauncher::launch,
                    onLoggedIn = {
                        val saveMemory = effect.saveMemory
                        viewModel.onViewIntent(HomeViewIntent.OnSaveMemory(saveMemory))
                    },
                    onFailure = {
                        viewModel.onViewIntent(HomeViewIntent.OnFailureCheckAuth(cause = it))
                    }
                )
            }

            is HomeViewEffect.NavigateToMemories -> {
                val route = MemoriesRoute(effect.ratioType.toRatio())
                memoriesNavigation.navigateToFeature(navController, route)
            }
        }
    }
}
