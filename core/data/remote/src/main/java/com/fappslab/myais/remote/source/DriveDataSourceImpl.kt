package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.remote.api.DriveService
import com.fappslab.myais.remote.model.DriveFileMetadata
import com.fappslab.myais.remote.model.DriveFolderRequest
import com.fappslab.myais.remote.model.mapper.toMemories
import com.fappslab.myais.remote.model.mapper.toMemory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

const val IMAGE_JPEG_MIME_TYPE = "image/jpeg"
const val IMAGE_PNG_MIME_TYPE = "image/png"
private const val FOLDER_MIME_TYPE = "application/vnd.google-apps.folder"

internal class DriveDataSourceImpl(
    private val service: DriveService,
    private val folderType: DriveFolderType = DriveFolderType.MyAIsMemories
) : DriveDataSource {

    override fun listFiles(): Flow<Memories> = flow {
        val spaces = if (folderType is DriveFolderType.AppDataFolder) {
            folderType.name
        } else "drive"

        val response = service.listFiles(spaces = spaces)
        emit(response.toMemories())
    }

    override fun deleteItem(itemType: DriverItemType): Flow<Boolean> = flow {
        val itemId = when (itemType) {
            DriverItemType.Folder -> {
                findFolderByName(folderType.name)
                    .takeIf { folderType !is DriveFolderType.AppDataFolder }
            }

            is DriverItemType.File -> {
                itemType.fileId
            }
        }

        val response = itemId?.let { service.deleteItem(itemId = it) }
        emit(response?.isSuccessful ?: false)
    }

    override fun uploadFile(save: SaveMemory): Flow<Memory> = flow {
        val folder = if (folderType !is DriveFolderType.AppDataFolder) {
            findFolderByName(folderType.name) ?: createFolder(folderType.name)
        } else folderType.name

        val file = requireNotNull(save.fileImage)
        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = file.asRequestBody(save.mimeType.toMediaTypeOrNull())
        )
        val metadata = DriveFileMetadata(
            name = file.name,
            mimeType = save.mimeType,
            parents = listOf(folder),
            description = save.description
        )
        val response = service.uploadFile(metadata, filePart)
        emit(response.toMemory())
    }

    private suspend fun createFolder(folderName: String): String {
        val request = DriveFolderRequest(
            name = folderName,
            mimeType = FOLDER_MIME_TYPE
        )
        val response = service.createFolder(request)
        return response.toMemory().id
    }

    private suspend fun findFolderByName(folderName: String): String? {
        val query = "mimeType='$FOLDER_MIME_TYPE' and name='$folderName' and 'me' in owners"
        val response = service.listFiles(query = query, spaces = "drive")
        return response.files?.firstOrNull()?.toMemory()?.id
    }
}

sealed class DriveFolderType {
    data object AppDataFolder : DriveFolderType()
    data object MyAIsMemories : DriveFolderType()
    data class CustomFolder(val spaces: String) : DriveFolderType();

    val name: String
        get() = when (this) {
            is AppDataFolder -> "appDataFolder"
            is MyAIsMemories -> "myAIsMemories"
            is CustomFolder -> spaces
        }
}
