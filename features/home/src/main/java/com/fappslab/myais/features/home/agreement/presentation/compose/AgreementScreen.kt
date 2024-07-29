package com.fappslab.myais.features.home.agreement.presentation.compose

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fappslab.myais.core.navigation.AgreementRoute
import com.fappslab.myais.core.navigation.HomeNavigation
import com.fappslab.myais.core.navigation.HomeRoute
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewEffect
import com.fappslab.myais.features.home.agreement.presentation.viewmodel.AgreementViewModel
import com.fappslab.myais.features.home.di.HomeModuleLoad
import com.fappslab.myais.libraries.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.libraries.arch.navigation.extension.LocalNavController
import com.fappslab.myais.libraries.arch.simplepermission.extension.openApplicationSettings
import com.fappslab.myais.libraries.arch.simplepermission.extension.openLinkInBrowser
import com.fappslab.myais.libraries.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun AgreementScreen() {
    KoinLazyModuleInitializer(HomeModuleLoad)
    val viewModel: AgreementViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    AgreementEffectObserve(viewModel.effect)

    PlutoTheme {
        AgreementContent(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
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

            AgreementViewEffect.NavigateToSettings -> {
                context.openApplicationSettings()
            }

            is AgreementViewEffect.NavigateToConditions -> {
                context.openLinkInBrowser(effect.link)
            }
        }
    }
}
