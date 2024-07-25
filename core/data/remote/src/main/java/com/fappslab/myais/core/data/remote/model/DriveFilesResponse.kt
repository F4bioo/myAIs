package com.fappslab.myais.core.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveFilesResponse(
    @SerializedName("files") val files: List<DriveFileResponse>?,
    @SerializedName("nextPageToken") val nextPageToken: String?
) {

    data class DriveFileResponse(
        @SerializedName("id") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("mimeType") val mimeType: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("parents") val parents: List<String>?,
        @SerializedName("webViewLink") val webViewLink: String?,
        @SerializedName("webContentLink") val webContentLink: String?,
        @SerializedName("createdTime") val createdTime: String?,
        @SerializedName("size") val size: Long?,
        @SerializedName("thumbnailLink") val thumbnailLink: String?
    )
}
