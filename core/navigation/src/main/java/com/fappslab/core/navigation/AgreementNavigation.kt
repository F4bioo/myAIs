package com.fappslab.core.navigation

import com.fappslab.myais.arch.navigation.FeatureRoute
import com.fappslab.myais.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface AgreementNavigation : FeatureRoute, NavigateRoute<AgreementRoute>

@Serializable
object AgreementRoute
