package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.builder.ContentBuilder
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class CreateContentUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(init: ContentBuilder.() -> Unit): Flow<Description> {
        val builder = ContentBuilder()
        builder.init()
        val parts = builder.build()
        return repository.generateContent(parts)
    }
}
