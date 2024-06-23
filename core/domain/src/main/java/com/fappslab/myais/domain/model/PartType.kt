package com.fappslab.myais.domain.model

sealed class PartType {
    data class Text(val textPrompt: String) : PartType()
    data class Image(val encodedImage: String) : PartType()
}
