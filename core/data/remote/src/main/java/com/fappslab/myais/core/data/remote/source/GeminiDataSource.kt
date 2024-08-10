package com.fappslab.myais.core.data.remote.source

import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.model.PartType
import com.fappslab.myais.core.domain.model.PromptType
import kotlinx.coroutines.flow.Flow

internal interface GeminiDataSource {
    fun getPrompt(promptType: PromptType): Flow<String>
    fun generateContent(model: String, parts: List<PartType>): Flow<Description>
}
