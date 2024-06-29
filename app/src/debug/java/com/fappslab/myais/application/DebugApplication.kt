package com.fappslab.myais.application

import com.fappslab.myais.debugtools.FlipperSetup
import timber.log.Timber

class DebugApplication : MainApplication() {

    override fun onCreate() {
        super.onCreate()
        FlipperSetup.start()
        Timber.plant(Timber.DebugTree())
    }
}
