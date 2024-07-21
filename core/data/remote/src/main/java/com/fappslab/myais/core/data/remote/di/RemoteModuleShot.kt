package com.fappslab.myais.core.data.remote.di

import com.fappslab.myais.libraries.arch.koin.koinshot.KoinShot
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import com.fappslab.myais.core.data.remote.BuildConfig
import com.fappslab.myais.core.data.remote.api.DriveService
import com.fappslab.myais.core.data.remote.api.GeminiService
import com.fappslab.myais.core.data.remote.api.PromptService
import com.fappslab.myais.core.data.remote.network.HttpClient
import com.fappslab.myais.core.data.remote.network.HttpClientImpl
import com.fappslab.myais.core.data.remote.network.retrofit.RetrofitClient
import com.fappslab.myais.core.data.remote.repository.MyAIsRepositoryImpl
import com.fappslab.myais.core.data.remote.source.DriveDataSourceImpl
import com.fappslab.myais.core.data.remote.source.GeminiDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val LOCAL_JSON_INTERCEPTORS_QUALIFIER = "LOCAL_JSON_INTERCEPTORS_QUALIFIER"
const val GEMINI_INTERCEPTORS_QUALIFIER = "GEMINI_INTERCEPTORS_QUALIFIER"
const val DRIVE_INTERCEPTORS_QUALIFIER = "DRIVE_INTERCEPTORS_QUALIFIER"

const val LOCAL_JSON_RETROFIT_QUALIFIER = "LOCAL_JSON_RETROFIT_QUALIFIER"
const val GEMINI_RETROFIT_QUALIFIER = "GEMINI_RETROFIT_QUALIFIER"
const val DRIVE_RETROFIT_QUALIFIER = "DRIVE_RETROFIT_QUALIFIER"

const val LOCAL_JSON_HTTP_CLIENT_QUALIFIER = "LOCAL_JSON_HTTP_CLIENT_QUALIFIER"
const val GEMINI_HTTP_CLIENT_QUALIFIER = "GEMINI_HTTP_CLIENT_QUALIFIER"
const val DRIVE_HTTP_CLIENT_QUALIFIER = "DRIVE_HTTP_CLIENT_QUALIFIER"

internal class RemoteModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single<Retrofit>(named(LOCAL_JSON_RETROFIT_QUALIFIER)) {
            RetrofitClient(
                baseUrl = BuildConfig.PROMPT_BASE_URL,
                interceptors = get(named(LOCAL_JSON_INTERCEPTORS_QUALIFIER))
            ).create()
        }
        single<Retrofit>(named(GEMINI_RETROFIT_QUALIFIER)) {
            RetrofitClient(
                baseUrl = BuildConfig.GEMINI_BASE_URL,
                interceptors = get(named(GEMINI_INTERCEPTORS_QUALIFIER))
            ).create()
        }
        single<Retrofit>(named(DRIVE_RETROFIT_QUALIFIER)) {
            RetrofitClient(
                baseUrl = BuildConfig.DRIVE_BASE_URL,
                interceptors = get(named(DRIVE_INTERCEPTORS_QUALIFIER))
            ).create()
        }

        single<HttpClient>(named(LOCAL_JSON_HTTP_CLIENT_QUALIFIER)) {
            HttpClientImpl(retrofit = get(named(LOCAL_JSON_RETROFIT_QUALIFIER)))
        }
        single<HttpClient>(named(GEMINI_HTTP_CLIENT_QUALIFIER)) {
            HttpClientImpl(retrofit = get(named(GEMINI_RETROFIT_QUALIFIER)))
        }
        single<HttpClient>(named(DRIVE_HTTP_CLIENT_QUALIFIER)) {
            HttpClientImpl(retrofit = get(named(DRIVE_RETROFIT_QUALIFIER)))
        }

        factory<MyAIsRepository> {
            MyAIsRepositoryImpl(
                geminiDataSource = GeminiDataSourceImpl(
                    promptService = get<HttpClient>(
                        named(LOCAL_JSON_HTTP_CLIENT_QUALIFIER)
                    ).create(PromptService::class.java),
                    geminiService = get<HttpClient>(
                        named(GEMINI_HTTP_CLIENT_QUALIFIER)
                    ).create(GeminiService::class.java),
                ),
                driveDataSource = DriveDataSourceImpl(
                    service = get<HttpClient>(
                        named(DRIVE_HTTP_CLIENT_QUALIFIER)
                    ).create(DriveService::class.java),
                ),
            )
        }
    }
}
