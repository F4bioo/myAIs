package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.model.PromptType
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class GetPromptUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(promptType: PromptType): Flow<String> {
        return repository.getPrompt(promptType)
    }
}
