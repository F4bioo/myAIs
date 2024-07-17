package com.fappslab.myais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fappslab.myais.arch.di.FEATURE_ROUTES_QUALIFIER
import com.fappslab.myais.arch.navigation.extension.NavHostControllerProvider
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.presentation.compose.MainNavGraph
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            PlutoTheme {
                Surface {
                    NavHostControllerProvider { navController ->
                        MainNavGraph(
                            navController = navController,
                            featureRoutes = koinInject(named(FEATURE_ROUTES_QUALIFIER))
                        )
                    }
                }
            }
        }
    }
}
