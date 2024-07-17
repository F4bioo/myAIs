package com.fappslab.myais.home.main.presentation.compose

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.fappslab.core.navigation.MemoriesNavigation
import com.fappslab.core.navigation.MemoriesRoute
import com.fappslab.myais.arch.auth.AuthManager
import com.fappslab.myais.arch.camerax.compose.CameraXPreviewProvider
import com.fappslab.myais.arch.camerax.compose.LocalCameraXPreview
import com.fappslab.myais.arch.camerax.compose.LocalPreviewView
import com.fappslab.myais.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.arch.navigation.extension.LocalNavController
import com.fappslab.myais.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.home.di.HomeModuleLoad
import com.fappslab.myais.home.main.presentation.compose.component.TopBarComponent
import com.fappslab.myais.home.main.presentation.extension.getAuthorizationClient
import com.fappslab.myais.home.main.presentation.model.AuthType
import com.fappslab.myais.home.main.presentation.model.MainStateType
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewEffect
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewIntent
import com.fappslab.myais.home.main.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private const val NAVIGATE_TO_MEMORIES_RESULT_CODE = 1000
private const val UPLOAD_MEMORY_RESULT_CODE = 1001

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    KoinLazyModuleInitializer(HomeModuleLoad)
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    HomeEffectObserve(viewModel)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                shouldShowNavigationIcon = state.mainStateType == MainStateType.Preview,
                isActionButtonEnabled = state.mainStateType.run {
                    this == MainStateType.Camera || this == MainStateType.Preview
                },
                onNavigationIconClicked = {
                    viewModel.onViewIntent(HomeViewIntent.OnNavigateToCamera)
                },
                onActionButtonClicked = {
                    viewModel.onViewIntent(HomeViewIntent.OnGoogleAuthMemories)
                }
            )
        }
    ) {
        CameraXPreviewProvider {
            HomeContent(
                paddingValues = it,
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
