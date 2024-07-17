package com.fappslab.myais.remote.model.mapper

import com.fappslab.myais.arch.extension.DateFormatType
import com.fappslab.myais.arch.extension.blankString
import com.fappslab.myais.arch.extension.orZero
import com.fappslab.myais.arch.extension.toFormatDate
import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.Owner
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.remote.model.DescriptionRequest
import com.fappslab.myais.remote.model.DescriptionRequest.Content
import com.fappslab.myais.remote.model.DescriptionResponse
import com.fappslab.myais.remote.model.DriveFileResponse
import com.fappslab.myais.remote.model.DriveFilesResponse
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
        .joinToString(separator = blankString()) { it.text }

    return Description(text = textContent.trim())
}

internal fun DriveFilesResponse.toMemories(): Memories {
    return Memories(
        owner = files.toOwner(),
        list = files.toMemories()
    )
}

private fun List<DriveFileResponse>?.toOwner(): Owner {
    val firstFile = this?.firstOrNull()
    return Owner(
        name = firstFile.extractAvatarName(),
        photoUrl = firstFile.extractAvatarPhoto(),
        parentFolderId = firstFile.extractParentFolder()
    )
}

private fun List<DriveFileResponse>?.toMemories(): List<Memory> {
    return this?.map { it.toMemory() }.orEmpty()
}

private fun DriveFileResponse?.extractAvatarName(): String {
    return this?.owners?.firstOrNull()?.displayName.orEmpty()
}

private fun DriveFileResponse?.extractAvatarPhoto(): String {
    return this?.owners?.firstOrNull()?.photoLink.orEmpty()
}

private fun DriveFileResponse?.extractParentFolder(): String {
    return this?.parents?.firstOrNull().orEmpty()
}

internal fun DriveFileResponse.toMemory(): Memory {
    return Memory(
        id = id.orEmpty(),
        size = size.orZero(),
        fileName = name.orEmpty(),
        mimeType = mimeType.orEmpty(),
        description = description.orEmpty(),
        webViewLink = webViewLink.orEmpty(),
        webContentLink = webContentLink.orEmpty(),
        thumbnailLink = thumbnailLink.toThumbnailSize(),
        createdTime = createdTime
            .toFormatDate(DateFormatType.ISO_8601_TO_READABLE)
            .orEmpty(),
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
