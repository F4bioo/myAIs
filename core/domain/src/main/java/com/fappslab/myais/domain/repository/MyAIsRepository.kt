package com.fappslab.myais.domain.repository

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.PromptType
import com.fappslab.myais.domain.model.SaveMemory
import kotlinx.coroutines.flow.Flow

interface MyAIsRepository {
    fun getPrompt(promptType: PromptType): Flow<String>
    fun generateContent(parts: List<PartType>): Flow<Description>

    fun listDriveFiles(): Flow<Memories>
    fun deleteDriveItem(itemType: DriverItemType): Flow<Boolean>
    fun uploadDriveFile(save: SaveMemory): Flow<Memory>
}

