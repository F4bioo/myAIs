package com.fappslab.myais.remote.repository

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.PromptType
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.domain.repository.MyAIsRepository
import com.fappslab.myais.remote.source.DriveDataSource
import com.fappslab.myais.remote.source.GeminiDataSource
import kotlinx.coroutines.flow.Flow

internal class MyAIsRepositoryImpl(
    private val geminiDataSource: GeminiDataSource,
    private val driveDataSource: DriveDataSource
) : MyAIsRepository {

    override fun getPrompt(promptType: PromptType): Flow<String> {
        return geminiDataSource.getPrompt(promptType)
    }

    override fun generateContent(model: String, parts: List<PartType>): Flow<Description> {
        return geminiDataSource.generateContent(model, parts)
    }

    override fun listDriveFiles(): Flow<Memories> {
        return driveDataSource.listFiles()
    }

    override fun deleteDriveItem(itemType: DriverItemType): Flow<Boolean> {
        return driveDataSource.deleteItem(itemType)
    }

    override fun uploadDriveFile(save: SaveMemory): Flow<Memory> {
        return driveDataSource.uploadFile(save)
    }
}
