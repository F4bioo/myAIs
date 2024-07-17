package com.fappslab.myais.remote.api

import com.fappslab.myais.remote.model.DescriptionPrompt
import retrofit2.http.GET

internal interface PromptService {

    @GET("prompts.json")
    suspend fun getPrompts(): DescriptionPrompt
}
