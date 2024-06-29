package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.builder.ContentBuilder
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.repository.MyAIsRepository

class CreateContentUseCase(
    private val repository: MyAIsRepository
) {

    suspend operator fun invoke(init: ContentBuilder.() -> Unit): Description {
        val builder = ContentBuilder()
        builder.init()
        val parts = builder.build()
        return repository.generateContent(parts)
    }
}
