package com.fappslab.myais.features.home.main.presentation.compose

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.fappslab.myais.core.navigation.MemoriesNavigation
import com.fappslab.myais.core.navigation.MemoriesRoute
import com.fappslab.myais.features.home.di.HomeModuleLoad
import com.fappslab.myais.features.home.main.presentation.extension.getAuthorizationClient
import com.fappslab.myais.features.home.main.presentation.model.AuthType
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

private const val NAVIGATE_TO_MEMORIES_RESULT_CODE = 1000
private const val UPLOAD_MEMORY_RESULT_CODE = 1001

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
    val resultCode = remember { mutableIntStateOf(RESULT_CANCELED) }
    val signInLauncher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            when (resultCode.intValue) {
                NAVIGATE_TO_MEMORIES_RESULT_CODE -> {
                    val authType = AuthType.NavigateToMemories
                    viewModel.onViewIntent(HomeViewIntent.OnGoogleAuth(authType))
                }

                UPLOAD_MEMORY_RESULT_CODE -> {
                    val authType = AuthType.UploadMemory
                    viewModel.onViewIntent(HomeViewIntent.OnGoogleAuth(authType))
                }
            }
        }
    }

    viewModel.effect.observeAsEvents { effect ->
        when (effect) {
            is HomeViewEffect.CheckAuthMemories -> {
                resultCode.intValue = NAVIGATE_TO_MEMORIES_RESULT_CODE
                authManager.getAuthorizationClient(
                    onLoggedOut = signInLauncher::launch,
                    onLoggedIn = {
                        val route = MemoriesRoute(effect.ratioType.toRatio())
                        memoriesNavigation.navigateToFeature(navController, route)
                    },
                    onFailure = {
                        viewModel.onViewIntent(HomeViewIntent.OnFailureCheckAuth(it))
                    }
                )
            }

            is HomeViewEffect.CheckAuthMemory -> {
                resultCode.intValue = UPLOAD_MEMORY_RESULT_CODE
                authManager.getAuthorizationClient(
                    onLoggedOut = signInLauncher::launch,
                    onLoggedIn = {
                        val saveMemory = effect.saveMemory
                        viewModel.onViewIntent(HomeViewIntent.OnSaveMemory(saveMemory))
                    },
                    onFailure = {
                        viewModel.onViewIntent(HomeViewIntent.OnFailureCheckAuth(it))
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
