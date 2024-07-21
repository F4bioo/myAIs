package com.fappslab.myais.libraries.arch.koin.koinlazy

import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import org.koin.android.scope.AndroidScopeComponent

interface KoinLazy : AndroidScopeComponent {
    val koinLoad: KoinLoad
}
