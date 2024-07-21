package com.fappslab.myais.libraries.arch.koin.koinshot

import android.content.Context
import androidx.startup.Initializer
import com.fappslab.myais.libraries.arch.koin.KoinModules
import org.koin.core.module.Module

abstract class KoinShot : Initializer<List<Module>>, KoinModules() {

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    override fun create(context: Context): List<Module> {
        ModuleInitializer.add(modules)
        return modules
    }
}
