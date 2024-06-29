package com.fappslab.myais.remote.model.mapper

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.DriveFile
import com.fappslab.myais.domain.model.DriveOwner
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.remote.model.DescriptionRequest
import com.fappslab.myais.remote.model.DescriptionRequest.Content
import com.fappslab.myais.remote.model.DescriptionResponse
import com.fappslab.myais.remote.model.DriveFileResponse
import com.fappslab.myais.remote.model.DriveFileResponse.DriveOwnerResponse
import com.fappslab.myais.remote.source.IMAGE_JPEG_MIME_TYPE

internal fun List<PartType>.toDescriptionRequest(): DescriptionRequest {
    val contentParts = map { part ->
        when (part) {
            is PartType.Text -> DescriptionRequest.Part(
                text = part.textPrompt
            )

            is PartType.Image -> DescriptionRequest.Part(
                inlineData = DescriptionRequest.InlineData(
                    encodedImage = part.encodedImage,
                    mimeType = IMAGE_JPEG_MIME_TYPE,
                )
            )
        }
    }

    return DescriptionRequest(
        contents = listOf(Content(parts = contentParts))
    )
}

internal fun DescriptionResponse.toDescription(): Description {
    val textContent = this.candidates
        .flatMap { it.content.parts }
        .joinToString(separator = " ") { it.text }

    return Description(text = textContent)
}

internal fun List<DriveFileResponse>?.toDriveFiles(): List<DriveFile> {
    return this?.map { it.toDriveFile() }.orEmpty()
}

internal fun DriveFileResponse.toDriveFile(): DriveFile {
    return DriveFile(
        id = id.orEmpty(),
        name = name.orEmpty(),
        mimeType = mimeType.orEmpty(),
        description = description.orEmpty(),
        parents = parents.orEmpty(),
        webViewLink = webViewLink.orEmpty(),
        webContentLink = webContentLink.orEmpty(),
        createdTime = createdTime.orEmpty(),
        owners = owners?.map { it.toDriveUser() }.orEmpty(),
        size = size ?: 0,
        thumbnailLink = thumbnailLink.toThumbnailSize()
    )
}

internal fun DriveOwnerResponse.toDriveUser(): DriveOwner {
    return DriveOwner(
        displayName = displayName.orEmpty(),
        emailAddress = emailAddress.orEmpty()
    )
}

private fun String?.toThumbnailSize(size: Int = 500): String {
    if (this.isNullOrEmpty()) return ""

    val regex = "=s\\d+".toRegex()
    val replacement = "=s$size"
    return if (this.contains(regex)) {
        this.replace(regex, replacement)
    } else "$this$replacement"
}
