package com.fappslab.myais.core.domain.model

data class Memories(
    val owner: Owner,
    val list: List<Memory>,
)

data class Owner(
    val name: String,
    val photoUrl: String,
    val parentFolderId: String,
)

data class Memory(
    val id: String,
    val size: Long,
    val fileName: String,
    val mimeType: String,
    val description: String,
    val createdTime: String,
    val webViewLink: String,
    val webContentLink: String,
    val thumbnailLink: String
)
