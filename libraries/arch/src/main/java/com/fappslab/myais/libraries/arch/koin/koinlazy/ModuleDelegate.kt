package com.fappslab.myais.libraries.arch.koin.koinlazy

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class ModuleDelegate(
    private val koinLoad: KoinLoad,
    owner: LifecycleOwner
) : ReadOnlyProperty<KoinLazy, KoinLoad>, DefaultLifecycleObserver {

    private var koinLazy: KoinLazy? = null

    init {
        koinLoad.load()
        owner.lifecycle.addObserver(this)
    }

    override fun getValue(thisRef: KoinLazy, property: KProperty<*>): KoinLoad {
        koinLazy = thisRef
        return koinLoad
    }

    override fun onDestroy(owner: LifecycleOwner) {
        koinLoad.unload()
        owner.lifecycle.removeObserver(this)
        super.onDestroy(owner)
    }
}

fun LifecycleOwner.subModules(koinLoad: KoinLoad): ReadOnlyProperty<KoinLazy, KoinLoad> {
    return ModuleDelegate(koinLoad, owner = this)
}
