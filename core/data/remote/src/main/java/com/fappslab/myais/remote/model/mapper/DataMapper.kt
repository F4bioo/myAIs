package com.fappslab.myais.remote.model.mapper

import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.remote.model.EyesightRequest
import com.fappslab.myais.remote.model.EyesightRequest.Content
import com.fappslab.myais.remote.model.EyesightResponse

internal fun List<PartType>.toEyesightRequest(): EyesightRequest {
    val contentParts = map { part ->
        when (part) {
            is PartType.Text -> EyesightRequest.Part(
                text = part.textPrompt
            )

            is PartType.Image -> EyesightRequest.Part(
                inlineData = EyesightRequest.InlineData(
                    encodedImage = part.encodedImage,
                    mimeType = "image/jpeg",
                )
            )
        }
    }

    return EyesightRequest(
        contents = listOf(Content(parts = contentParts))
    )
}

internal fun EyesightResponse.toEyesight(): Eyesight {
    val textContent = this.candidates
        .flatMap { it.content.parts }
        .joinToString(" ") { it.text }

    return Eyesight(text = textContent)
}
