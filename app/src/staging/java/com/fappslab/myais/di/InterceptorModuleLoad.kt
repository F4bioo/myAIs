package com.fappslab.myais.di

import com.fappslab.myais.core.data.remote.BuildConfig
import com.fappslab.myais.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.myais.core.data.remote.di.DRIVE_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.di.GEMINI_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.di.LOCAL_JSON_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.core.data.remote.network.interceptor.ApiKeyInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.AuthInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.HeadersInterceptor
import com.fappslab.myais.core.data.remote.network.interceptor.LocalJsonInterceptor
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

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
                HeadersInterceptor(androidApplication())
            )
        }
        single(named(DRIVE_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                AuthInterceptor(authManager = get()),
                HeadersInterceptor(androidApplication())
            )
        }
    }
}
