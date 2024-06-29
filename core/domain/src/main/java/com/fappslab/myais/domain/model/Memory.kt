package com.fappslab.myais.domain.model

data class Memory(
    val id: String,
    val name: String,
    val mimeType: String,
    val description: String,
    val parents: List<String>,
    val webContentLink: String,
    val createdTime: Long,
    val size: Long,
    val thumbnailLink: String,
)
