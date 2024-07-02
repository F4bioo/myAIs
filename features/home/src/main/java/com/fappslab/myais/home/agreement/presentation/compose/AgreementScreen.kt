package com.fappslab.myais.home.agreement.presentation.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.fappslab.core.navigation.HomeNavigation
import com.fappslab.myais.arch.koin.koinlazy.extension.KoinLazyModuleInitializer
import com.fappslab.myais.arch.simplepermission.extension.openApplicationSettings
import com.fappslab.myais.arch.viewmodel.extension.observeAsEvents
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewEffect
import com.fappslab.myais.home.agreement.presentation.viewmodel.AgreementViewModel
import com.fappslab.myais.home.di.HomeModuleKoinLoad
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AgreementScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeNavigation: HomeNavigation
) {
    KoinLazyModuleInitializer(HomeModuleKoinLoad)
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
    viewEffect: Flow<AgreementViewEffect>
) {
    val context = LocalContext.current

    viewEffect.observeAsEvents { effect ->
        when (effect) {
            AgreementViewEffect.NavigateToHome -> {

            }

            AgreementViewEffect.NavigateToPrivacyPolicy -> {
                val url = "https://fappslab.com/myAIs/terms.html"
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

            AgreementViewEffect.NavigateToSettings -> {
                context.openApplicationSettings()
            }
        }
    }
}
