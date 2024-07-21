package com.fappslab.myais.core.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveFileMetadata(
    @SerializedName("name") val name: String,
    @SerializedName("mimeType") val mimeType: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("parents") val parents: List<String>? = null,
    @SerializedName("starred") val starred: Boolean? = null,
    @SerializedName("viewersCanCopyContent") val viewersCanCopyContent: Boolean? = null,
    @SerializedName("writersCanShare") val writersCanShare: Boolean? = null,
    @SerializedName("properties") val properties: Map<String, String>? = null
)
