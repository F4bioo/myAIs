package com.fappslab.myais.presentation.compose

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.fappslab.myais.core.navigation.AgreementRoute
import com.fappslab.myais.core.navigation.HomeRoute
import com.fappslab.myais.libraries.arch.navigation.FeatureRoute

@Composable
fun MainNavGraph(
    navController: NavHostController,
    featureRoutes: List<FeatureRoute>
) {
    val context = LocalContext.current
    val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    NavHost(
        navController = navController,
        startDestination = if (isCameraPermissionGranted) HomeRoute else AgreementRoute
    ) {
        featureRoutes.forEach { featureRoute ->
            featureRoute.register(navGraphBuilder = this)
        }
    }
}
