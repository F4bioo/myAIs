package com.fappslab.myais.debugtools

import android.content.Context
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.FlipperLeakEventListener
import com.facebook.soloader.SoLoader
import com.fappslab.myais.di.FLIPPER_PLUGINS_QUALIFIER
import leakcanary.LeakCanary
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

object FlipperSetup : KoinComponent {

    private val context: Context by inject()

    init {
        setupLeakCanaryPlugin()
        SoLoader.init(context, false)
    }

    fun start() {
        if (FlipperUtils.shouldEnableFlipper(context)) {
            val client: FlipperClient = get()
            val plugins: List<FlipperPlugin> = get(named(FLIPPER_PLUGINS_QUALIFIER))
            plugins.forEach { client.addPlugin(it) }
            client.start()
        }
    }

    private fun setupLeakCanaryPlugin() {
        LeakCanary.config = LeakCanary.config.run {
            copy(eventListeners = eventListeners + FlipperLeakEventListener())
        }
    }
}
