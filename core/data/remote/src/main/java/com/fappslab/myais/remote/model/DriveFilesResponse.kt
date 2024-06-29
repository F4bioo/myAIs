package com.fappslab.myais.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveFilesResponse(
    @SerializedName("files") val files: List<DriveFileResponse>?,
    @SerializedName("nextPageToken") val nextPageToken: String?
)
