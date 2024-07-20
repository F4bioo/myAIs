package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.PromptType
import com.fappslab.myais.remote.api.GeminiService
import com.fappslab.myais.remote.api.PromptService
import com.fappslab.myais.remote.model.mapper.toDescription
import com.fappslab.myais.remote.model.mapper.toDescriptionRequest
import com.fappslab.myais.remote.network.exception.extension.parseHttpError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

internal class GeminiDataSourceImpl(
    private val promptService: PromptService,
    private val geminiService: GeminiService
) : GeminiDataSource {

    override fun getPrompt(promptType: PromptType): Flow<String> =
        flow {
            val response = promptService.getPrompts()
            val result = when (promptType) {
                PromptType.ImageDescription -> response.prompts.imageDescription
            }
            val locale = Locale.getDefault()
            emit(result.format(locale.displayName))
        }

    override fun generateContent(model: String, parts: List<PartType>): Flow<Description> =
        flow {
            val request = parts.toDescriptionRequest()
            val result = geminiService.generateContent(model, request)
            val description = result.toDescription()
            emit(description)
        }.parseHttpError()
}
