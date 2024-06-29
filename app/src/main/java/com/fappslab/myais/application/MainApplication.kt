package com.fappslab.myais.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.fappslab.myais.di.InterceptorModuleLoad
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

open class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }

    private fun startKoin() {
        startKoin(KoinDeclaration.get(this))
        koinLoad()
    }

    private fun koinLoad() {
        InterceptorModuleLoad.load()
    }
}
