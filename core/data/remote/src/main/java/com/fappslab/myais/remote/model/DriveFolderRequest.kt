package com.fappslab.myais.remote.model

import com.google.gson.annotations.SerializedName

internal data class DriveFolderRequest(
    @SerializedName("name") val name: String,
    @SerializedName("mimeType") val mimeType: String
)
