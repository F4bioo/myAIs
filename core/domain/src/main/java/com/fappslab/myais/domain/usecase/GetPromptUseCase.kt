package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.model.PromptType
import com.fappslab.myais.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class GetPromptUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(promptType: PromptType): Flow<String> {
        return repository.getPrompt(promptType)
    }
}
