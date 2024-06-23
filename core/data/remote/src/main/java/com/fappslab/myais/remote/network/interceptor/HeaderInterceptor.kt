package com.fappslab.myais.remote.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val USER_AGENT =
    "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.181 Mobile Safari/537.36"

internal class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Connection", "close")
            .addHeader("User-Agent", USER_AGENT)
            .removeHeader("Content-Length")
            .build()

        return chain.proceed(request)
    }
}
