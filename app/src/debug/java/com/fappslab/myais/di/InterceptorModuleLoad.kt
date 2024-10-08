package com.fappslab.myais.di

import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.fappslab.myais.core.data.remote.BuildConfig
import com.fappslab.myais.core.data.remote.di.DRIVE_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.di.GEMINI_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.di.LOCAL_JSON_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.network.interceptor.ApiKeyInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.AuthInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.HeadersInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.LocalJsonInterceptor
import com.fappslab.myais.debugtools.SharedPreferencesPlugin.getDescriptors
import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val FLIPPER_PLUGINS_QUALIFIER = "FLIPPER_PLUGINS_QUALIFIER"

object InterceptorModuleLoad : KoinLoad() {

    override val dataModule = module {
        single(named(LOCAL_JSON_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                LocalJsonInterceptor(androidApplication()),
            )
        }
        single(named(GEMINI_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                ApiKeyInterceptor(BuildConfig.GEMINI_API_KEY),
                HeadersInterceptor(androidApplication()),
                get<FlipperOkhttpInterceptor>()
            )
        }
        single(named(DRIVE_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                AuthInterceptor(authManager = get()),
                HeadersInterceptor(androidApplication()),
                get<FlipperOkhttpInterceptor>()
            )
        }
    }

    override val additionalModule = module {
        single { NetworkFlipperPlugin() }
        single { AndroidFlipperClient.getInstance(androidContext()) }
        single { FlipperOkhttpInterceptor(get<NetworkFlipperPlugin>(), true) }
        single(named(FLIPPER_PLUGINS_QUALIFIER)) {
            listOf<FlipperPlugin>(
                SharedPreferencesFlipperPlugin(androidContext(), getDescriptors(androidContext())),
                InspectorFlipperPlugin(androidContext(), DescriptorMapping.withDefaults()),
                DatabasesFlipperPlugin(androidContext()),
                NavigationFlipperPlugin.getInstance(),
                CrashReporterPlugin.getInstance(),
                LeakCanary2FlipperPlugin(),
                get<NetworkFlipperPlugin>()
            )
        }
    }
}
