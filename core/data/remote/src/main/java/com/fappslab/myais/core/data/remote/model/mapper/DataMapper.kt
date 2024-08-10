package com.fappslab.myais.core.data.remote.model.mapper

import com.fappslab.myais.core.data.remote.model.DescriptionRequest
import com.fappslab.myais.core.data.remote.model.DescriptionRequest.Content
import com.fappslab.myais.core.data.remote.model.DescriptionResponse
import com.fappslab.myais.core.data.remote.model.DriveFilesResponse
import com.fappslab.myais.core.data.remote.model.DriveFilesResponse.DriveFileResponse
import com.fappslab.myais.core.data.remote.model.DriveUserResponse
import com.fappslab.myais.core.data.remote.source.IMAGE_JPEG_MIME_TYPE
import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.model.Memories
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.core.domain.model.PartType
import com.fappslab.myais.libraries.arch.extension.DateFormatType
import com.fappslab.myais.libraries.arch.extension.blankString
import com.fappslab.myais.libraries.arch.extension.orZero
import com.fappslab.myais.libraries.arch.extension.toFormatDate

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

internal fun DriveUserResponse.toOwner(): Owner {
    return Owner(
        name = user.displayName,
        email = user.emailAddress,
        photoUrl = user.photoLink
    )
}

internal fun DriveFilesResponse.toMemories(): Memories {
    return Memories(
        list = files.toMemories(),
        nextPageToken = nextPageToken
    )
}

private fun List<DriveFileResponse>?.toMemories(): List<Memory> {
    return this?.map { it.toMemory() }.orEmpty()
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
        parentFolderId = parents?.firstOrNull().orEmpty(),
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
