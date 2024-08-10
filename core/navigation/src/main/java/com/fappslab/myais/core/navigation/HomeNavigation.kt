package com.fappslab.myais.core.navigation

import com.fappslab.myais.libraries.arch.navigation.FeatureRoute
import com.fappslab.myais.libraries.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface HomeNavigation : FeatureRoute, NavigateRoute<HomeRoute>

@Serializable
object HomeRoute
