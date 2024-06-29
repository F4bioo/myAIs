package com.fappslab.myais.remote.repository

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.DriveFile
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.domain.repository.MyAIsRepository
import com.fappslab.myais.remote.source.DriveDataSource
import com.fappslab.myais.remote.source.GeminiDataSource

internal class MyAIsRepositoryImpl(
    private val geminiDataSource: GeminiDataSource,
    private val driveDataSource: DriveDataSource
) : MyAIsRepository {

    override suspend fun generateContent(parts: List<PartType>): Description {
        return geminiDataSource.generateContent(parts)
    }

    override suspend fun listDriveFiles(): List<DriveFile> {
        return driveDataSource.listFiles()
    }

    override suspend fun deleteDriveItem(itemType: DriverItemType): Boolean {
        return driveDataSource.deleteItem(itemType)
    }

    override suspend fun uploadDriveFile(save: SaveMemory): DriveFile {
        return driveDataSource.uploadFile(save)
    }
}
