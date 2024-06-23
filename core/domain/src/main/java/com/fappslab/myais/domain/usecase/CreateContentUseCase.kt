package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.builder.ContentBuilder
import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.repository.GetEyesightRepository

class CreateContentUseCase(
    private val repository: GetEyesightRepository
) {

    suspend operator fun invoke(init: ContentBuilder.() -> Unit): Eyesight {
        val builder = ContentBuilder()
        builder.init()
        val parts = builder.build()
        return repository.getEyesight(parts)
    }
}
