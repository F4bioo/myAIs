package com.fappslab.myais.core.data.remote.network.interceptor

import com.fappslab.myais.libraries.arch.auth.AuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { authManager.getAccessToken() }
        val requestBuilder = chain.request().newBuilder()
        val newBuilder = accessToken?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } ?: requestBuilder
        return chain.proceed(newBuilder.build())
    }
}
