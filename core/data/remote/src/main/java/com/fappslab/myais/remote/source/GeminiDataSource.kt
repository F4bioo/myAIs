package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.PromptType
import kotlinx.coroutines.flow.Flow

internal interface GeminiDataSource {
    fun getPrompt(promptType: PromptType): Flow<String>
    fun generateContent(parts: List<PartType>): Flow<Description>
}
