package com.fappslab.myais.arch.navigation.extension

import com.fappslab.myais.arch.navigation.FeatureRoute
import org.koin.core.module.Module
import org.koin.core.qualifier.named

inline fun <reified T : FeatureRoute> Module.provideFeatureRoute(
    crossinline instance: () -> T
) {
    factory<T> { instance() }
    factory<FeatureRoute>(named(T::class.java.name)) { get<T>() }
}
