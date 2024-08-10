package com.fappslab.myais.core.data.remote.api

import com.fappslab.myais.core.data.remote.model.DescriptionPrompt
import retrofit2.http.GET

internal interface PromptService {

    @GET("prompts.json")
    suspend fun getPrompts(): DescriptionPrompt
}
