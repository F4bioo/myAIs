package com.fappslab.myais.core.data.remote.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val API_KEY_PARAM = "key"

class ApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter(API_KEY_PARAM, apiKey)
            .build()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
