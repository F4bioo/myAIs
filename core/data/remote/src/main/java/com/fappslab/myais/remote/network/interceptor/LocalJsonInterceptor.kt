package com.fappslab.myais.remote.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LocalJsonInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath.endsWith("prompts.json")) {
            val inputStream = context.assets.open("prompts.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }
        return chain.proceed(request)
    }
}
