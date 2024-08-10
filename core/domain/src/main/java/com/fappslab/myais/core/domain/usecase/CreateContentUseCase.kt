package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.builder.ContentBuilder
import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class CreateContentUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(init: ContentBuilder.() -> Unit): Flow<Description> {
        val builder = ContentBuilder()
        builder.init()
        val (modelType, parts) = builder.build()
        return repository.generateContent(modelType.model, parts)
    }
}
