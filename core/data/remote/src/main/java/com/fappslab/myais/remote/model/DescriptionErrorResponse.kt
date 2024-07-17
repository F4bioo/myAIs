package com.fappslab.myais.remote.model


import com.google.gson.annotations.SerializedName

data class DescriptionErrorResponse(
    @SerializedName("error") val error: Error
) {

    data class Error(
        @SerializedName("code") val code: Int,
        @SerializedName("details") val details: List<Detail>,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: String
    )

    data class Detail(
        @SerializedName("domain") val domain: String,
        @SerializedName("metadata") val metadata: Metadata,
        @SerializedName("reason") val reason: String,
        @SerializedName("@type") val type: String
    )

    data class Metadata(
        @SerializedName("service") val service: String
    )
}
