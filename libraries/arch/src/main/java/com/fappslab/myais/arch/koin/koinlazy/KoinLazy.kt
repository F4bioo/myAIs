package com.fappslab.myais.arch.koin.koinlazy

import com.fappslab.myais.arch.koin.koinload.KoinLoad
import org.koin.android.scope.AndroidScopeComponent

interface KoinLazy : AndroidScopeComponent {
    val koinLoad: KoinLoad
}
