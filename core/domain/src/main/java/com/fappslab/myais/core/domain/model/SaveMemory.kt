package com.fappslab.myais.core.domain.model

import java.io.File

data class SaveMemory(
    val description: String?,
    val mimeType: String,
    val fileImage: File?
)
