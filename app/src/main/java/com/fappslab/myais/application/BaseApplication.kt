package com.fappslab.myais.application

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class BaseApplication : Application() {

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
    }
}
