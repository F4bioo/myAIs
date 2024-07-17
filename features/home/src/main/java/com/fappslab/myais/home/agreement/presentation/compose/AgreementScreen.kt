package com.fappslab.myais.home.agreement.presentation.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fappslab.core.navigation.AgreementRoute
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.core.navigation.HomeRoute
import com.fappslab.myais.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.arch.navigation.extension.LocalNavController
import com.fappslab.myais.arch.simplepermission.extension.openApplicationSettings
import com.fappslab.myais.arch.simplepermission.extension.openLinkInBrowser
import com.fappslab.myais.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewEffect
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewModel
import com.fappslab.myais.home.di.HomeModuleLoad
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private const val PRIVACY_POLICY_URL = "https://fappslab.com/myAIs/terms.html"

@Composable
internal fun AgreementScreen(
    modifier: Modifier = Modifier,
) {
    KoinLazyModuleInitializer(HomeModuleLoad)
    val viewModel: AgreementViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    AgreementEffectObserve(viewModel.effect)

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        AgreementContent(
            paddingValues = it,
            state = state,
            intent = viewModel::onViewIntent
        )
    }
}

@Composable
private fun AgreementEffectObserve(
    viewEffect: Flow<AgreementViewEffect>,
    homeNavigation: HomeNavigation = koinInject()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    viewEffect.observeAsEvents { effect ->
        when (effect) {
            AgreementViewEffect.NavigateToHome -> {
                homeNavigation.navigateToFeature(navController, HomeRoute) {
                    popUpTo(AgreementRoute) { inclusive = true }
                }
            }

            AgreementViewEffect.NavigateToPrivacyPolicy -> {
                context.openLinkInBrowser(PRIVACY_POLICY_URL)
            }

            AgreementViewEffect.NavigateToSettings -> {
                context.openApplicationSettings()
            }
        }
    }
}
