package com.fappslab.myais.core.domain.model

data class Memories(
    val list: List<Memory>,
    val nextPageToken: String?
)

data class Owner(
    val name: String,
    val email: String,
    val photoUrl: String,
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
    val thumbnailLink: String,
    val parentFolderId: String,
)
