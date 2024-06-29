package com.fappslab.myais.remote.di

import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.domain.repository.MyAIsRepository
import com.fappslab.myais.remote.BuildConfig
import com.fappslab.myais.remote.api.DriveService
import com.fappslab.myais.remote.api.GeminiService
import com.fappslab.myais.remote.network.HttpClient
import com.fappslab.myais.remote.network.HttpClientImpl
import com.fappslab.myais.remote.network.retrofit.RetrofitClient
import com.fappslab.myais.remote.repository.MyAIsRepositoryImpl
import com.fappslab.myais.remote.source.DriveDataSourceImpl
import com.fappslab.myais.remote.source.GeminiDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val GEMINI_INTERCEPTORS_QUALIFIER = "GEMINI_INTERCEPTORS_QUALIFIER"
const val DRIVE_INTERCEPTORS_QUALIFIER = "DRIVE_INTERCEPTORS_QUALIFIER"
const val GEMINI_RETROFIT_QUALIFIER = "GEMINI_RETROFIT_QUALIFIER"
const val DRIVE_RETROFIT_QUALIFIER = "DRIVE_RETROFIT_QUALIFIER"
const val GEMINI_HTTP_CLIENT_QUALIFIER = "GEMINI_HTTP_CLIENT_QUALIFIER"
const val DRIVE_HTTP_CLIENT_QUALIFIER = "DRIVE_HTTP_CLIENT_QUALIFIER"

internal class RemoteModuleShot : KoinShot() {

    override val dataModule: Module = module {
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

        single<HttpClient>(named(GEMINI_HTTP_CLIENT_QUALIFIER)) {
            HttpClientImpl(retrofit = get(named(GEMINI_RETROFIT_QUALIFIER)))
        }
        single<HttpClient>(named(DRIVE_HTTP_CLIENT_QUALIFIER)) {
            HttpClientImpl(retrofit = get(named(DRIVE_RETROFIT_QUALIFIER)))
        }

        factory<MyAIsRepository> {
            MyAIsRepositoryImpl(
                geminiDataSource = GeminiDataSourceImpl(
                    service = get<HttpClient>(
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
