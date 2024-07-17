package com.fappslab.myais.arch.navigation

import androidx.navigation.NavGraphBuilder

interface FeatureRoute {
    fun register(navGraphBuilder: NavGraphBuilder)
}
