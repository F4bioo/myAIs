package com.fappslab.myais.remote.network.interceptor

import com.fappslab.myais.remote.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY_PARAM = "key"

internal class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter(API_KEY_PARAM, BuildConfig.GEMINI_API_KEY)
            .build()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
