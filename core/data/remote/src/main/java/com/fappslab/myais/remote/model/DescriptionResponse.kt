package com.fappslab.myais.remote.model

import com.google.gson.annotations.SerializedName

internal data class DescriptionResponse(
    @SerializedName("candidates") val candidates: List<Candidate>
) {

    data class Candidate(
        @SerializedName("content") val content: Content
    ) {

        data class Content(
            @SerializedName("parts") val parts: List<Part>
        ) {

            data class Part(
                @SerializedName("text") val text: String
            )
        }
    }
}
