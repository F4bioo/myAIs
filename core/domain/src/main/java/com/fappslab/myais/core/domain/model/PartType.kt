package com.fappslab.myais.core.domain.model

sealed class PartType {
    data class Text(val textPrompt: String) : PartType()
    data class Image(val encodedImage: String) : PartType()
}
