package com.fappslab.core.navigation

import com.fappslab.myais.arch.navigation.FeatureRoute
import com.fappslab.myais.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface HomeNavigation : FeatureRoute, NavigateRoute<HomeRoute>

@Serializable
object HomeRoute
