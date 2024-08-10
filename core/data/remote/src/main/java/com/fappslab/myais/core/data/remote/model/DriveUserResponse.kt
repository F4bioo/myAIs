package com.fappslab.myais.core.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveUserResponse(
    @SerializedName("user") val user: OwnerResponse
) {

    data class OwnerResponse(
        @SerializedName("displayName") val displayName: String,
        @SerializedName("emailAddress") val emailAddress: String,
        @SerializedName("photoLink") val photoLink: String
    )
}
