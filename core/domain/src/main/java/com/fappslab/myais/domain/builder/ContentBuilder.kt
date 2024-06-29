package com.fappslab.myais.domain.builder

import com.fappslab.myais.domain.model.PartType

class ContentBuilder {
    private val parts = mutableListOf<PartType>()

    fun text(textPrompt: String) = apply {
        parts.add(PartType.Text(textPrompt))
    }

    fun image(encodedImage: String) = apply {
        parts.add(PartType.Image(encodedImage))
    }

    fun build(): List<PartType> = parts
}
