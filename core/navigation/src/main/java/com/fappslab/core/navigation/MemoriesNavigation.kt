package com.fappslab.core.navigation

import com.fappslab.myais.arch.navigation.FeatureRoute
import com.fappslab.myais.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface MemoriesNavigation : FeatureRoute, NavigateRoute<MemoriesRoute>

@Serializable
data class MemoriesRoute(
    val aspectRatio: Float
)
