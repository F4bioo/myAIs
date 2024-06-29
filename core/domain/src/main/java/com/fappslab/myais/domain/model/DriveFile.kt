package com.fappslab.myais.domain.model

data class DriveFile(
    val id: String,
    val name: String,
    val mimeType: String,
    val description: String,
    val parents: List<String>,
    val webViewLink: String,
    val webContentLink: String,
    val createdTime: String,
    val owners: List<DriveOwner>,
    val size: Long,
    val thumbnailLink: String
)

data class DriveOwner(
    val displayName: String,
    val emailAddress: String
)
