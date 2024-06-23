package com.fappslab.myais.remote.network.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val READ_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 10L
private const val CONNECT_TIMEOUT = 15L

internal class RetrofitClient(
    private val baseUrl: String,
    private val interceptors: List<Interceptor>
) {

    fun create(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient())
            .build()
    }

    private fun okhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)

        return interceptors.fold(builder) { acc, interceptor ->
            acc.addInterceptor(interceptor)
        }.build()
    }
}
