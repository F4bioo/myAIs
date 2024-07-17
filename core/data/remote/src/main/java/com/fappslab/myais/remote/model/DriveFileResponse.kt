package com.fappslab.myais.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveFileResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("mimeType") val mimeType: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("parents") val parents: List<String>?,
    @SerializedName("webViewLink") val webViewLink: String?,
    @SerializedName("webContentLink") val webContentLink: String?,
    @SerializedName("createdTime") val createdTime: String?,
    @SerializedName("owners") val owners: List<DriveOwnerResponse>?,
    @SerializedName("size") val size: Long?,
    @SerializedName("thumbnailLink") val thumbnailLink: String?
) {

    data class DriveOwnerResponse(
        @SerializedName("displayName") val displayName: String?,
        @SerializedName("emailAddress") val emailAddress: String?,
        @SerializedName("photoLink") val photoLink: String?,
    )
}
