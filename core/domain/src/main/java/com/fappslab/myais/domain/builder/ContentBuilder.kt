package com.fappslab.myais.domain.builder

import com.fappslab.myais.domain.model.ModelType
import com.fappslab.myais.domain.model.PartType

class ContentBuilder {
    private var modelType: ModelType = ModelType.GEMINI_1_5_FLASH
    private val parts = mutableListOf<PartType>()

    fun text(textPrompt: String) = apply {
        parts.add(PartType.Text(textPrompt))
    }

    fun image(encodedImage: String) = apply {
        parts.add(PartType.Image(encodedImage))
    }

    fun model(modelType: ModelType) = apply {
        this.modelType = modelType
    }

    fun build(): Pair<ModelType, List<PartType>> = modelType to parts
}
