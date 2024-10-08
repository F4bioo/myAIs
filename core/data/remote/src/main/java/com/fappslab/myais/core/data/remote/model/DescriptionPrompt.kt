package com.fappslab.myais.core.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class DescriptionPrompt(
    @SerializedName("prompts") val prompts: Prompts
) {

    data class Prompts(
        @SerializedName("imageDescription") val imageDescription: String
    )
}
