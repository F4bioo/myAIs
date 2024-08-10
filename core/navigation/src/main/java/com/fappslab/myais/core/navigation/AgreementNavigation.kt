package com.fappslab.myais.core.navigation

import com.fappslab.myais.libraries.arch.navigation.FeatureRoute
import com.fappslab.myais.libraries.arch.navigation.NavigateRoute
import kotlinx.serialization.Serializable

interface AgreementNavigation : FeatureRoute, NavigateRoute<AgreementRoute>

@Serializable
object AgreementRoute
