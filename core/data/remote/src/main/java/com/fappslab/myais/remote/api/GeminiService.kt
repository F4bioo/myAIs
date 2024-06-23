package com.fappslab.myais.remote.api

import com.fappslab.myais.remote.model.EyesightRequest
import com.fappslab.myais.remote.model.EyesightResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface GeminiService {

    @POST("v1beta/models/gemini-pro-vision:generateContent")
    suspend fun generateContent(
        @Body request: EyesightRequest
    ): EyesightResponse
}
