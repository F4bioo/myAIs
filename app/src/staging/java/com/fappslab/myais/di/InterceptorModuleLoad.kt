package com.fappslab.myais.di

import com.fappslab.myais.arch.koin.koinload.KoinLoad
import com.fappslab.myais.remote.di.DRIVE_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.remote.di.GEMINI_INTERCEPTORS_QUALIFIER
import com.fappslab.myais.remote.network.interceptor.ApiKeyInterceptor
import com.fappslab.myais.remote.network.interceptor.AuthInterceptor
import com.fappslab.myais.remote.network.interceptor.HeadersInterceptor
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object InterceptorModuleLoad : KoinLoad() {

    override val dataModule = module {
        single(named(GEMINI_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                ApiKeyInterceptor(),
                HeadersInterceptor(androidApplication())
            )
        }
        single(named(DRIVE_INTERCEPTORS_QUALIFIER)) {
            listOf<Interceptor>(
                AuthInterceptor(androidApplication()),
                HeadersInterceptor(androidApplication())
            )
        }
    }
}
