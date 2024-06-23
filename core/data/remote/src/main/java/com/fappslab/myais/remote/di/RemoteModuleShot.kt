package com.fappslab.myais.remote.di

import com.fappslab.myais.arch.koin.KoinQualifier
import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.domain.repository.GetEyesightRepository
import com.fappslab.myais.remote.BuildConfig
import com.fappslab.myais.remote.api.GeminiService
import com.fappslab.myais.remote.network.HttpClient
import com.fappslab.myais.remote.network.HttpClientImpl
import com.fappslab.myais.remote.network.interceptor.ApiKeyInterceptor
import com.fappslab.myais.remote.network.interceptor.HeaderInterceptor
import com.fappslab.myais.remote.network.interceptor.LoggingInterceptor
import com.fappslab.myais.remote.network.retrofit.RetrofitClient
import com.fappslab.myais.remote.repository.GetEyesightRepositoryImpl
import com.fappslab.myais.remote.source.GetEyesightDataSourceImpl
import okhttp3.Interceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object RetrofitInterceptorQualifier : KoinQualifier

internal class RemoteModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single(qualifier = RetrofitInterceptorQualifier) {
            listOf<Interceptor>(
                ApiKeyInterceptor(),
                HeaderInterceptor(),
                LoggingInterceptor()
            )
        }

        single<Retrofit> {
            RetrofitClient(
                baseUrl = BuildConfig.GEMINI_BASE_URL,
                interceptors = get(qualifier = RetrofitInterceptorQualifier)
            ).create()
        }

        single<HttpClient> { HttpClientImpl(retrofit = get()) }

        factory<GetEyesightRepository> {
            GetEyesightRepositoryImpl(
                dataSource = GetEyesightDataSourceImpl(
                    service = get<HttpClient>().create(GeminiService::class.java)
                )
            )
        }
    }
}
