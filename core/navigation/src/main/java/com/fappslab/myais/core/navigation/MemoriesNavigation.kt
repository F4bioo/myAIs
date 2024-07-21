package com.fappslab.myais.core.navigation

import com.fappslab.myais.libraries.arch.navigation.FeatureRoute
import com.fappslab.myais.libraries.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface MemoriesNavigation : FeatureRoute, NavigateRoute<MemoriesRoute>

@Serializable
data class MemoriesRoute(
    val aspectRatio: Float
)
