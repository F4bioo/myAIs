package com.fappslab.myais.core.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class DescriptionRequest(
    @SerializedName("contents") val contents: List<Content>
) {

    data class Content(
        @SerializedName("parts") val parts: List<Part>
    )

    data class Part(
        @SerializedName("text") val text: String? = null,
        @SerializedName("inline_data") val inlineData: InlineData? = null
    )

    data class InlineData(
        @SerializedName("mime_type") val mimeType: String,
        @SerializedName("data") val encodedImage: String
    )
}
