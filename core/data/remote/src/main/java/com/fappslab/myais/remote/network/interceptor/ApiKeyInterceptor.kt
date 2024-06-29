package com.fappslab.myais.remote.network.interceptor

import androidx.annotation.Keep
import com.fappslab.myais.remote.BuildConfig
import com.google.android.gms.common.annotation.KeepName
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val API_KEY_PARAM = "key"

@Keep
class ApiKeyInterceptor : Interceptor {

    @Throws(IOException::class)
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
