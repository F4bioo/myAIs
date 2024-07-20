package com.fappslab.myais.remote.api

import com.fappslab.myais.remote.model.DescriptionRequest
import com.fappslab.myais.remote.model.DescriptionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface GeminiService {

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Body request: DescriptionRequest
    ): DescriptionResponse
}
