package com.fappslab.myais.core.domain.repository

import com.fappslab.myais.core.domain.model.Description
import com.fappslab.myais.core.domain.model.DriverItemType
import com.fappslab.myais.core.domain.model.Memories
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.PartType
import com.fappslab.myais.core.domain.model.PromptType
import com.fappslab.myais.core.domain.model.SaveMemory
import kotlinx.coroutines.flow.Flow

interface MyAIsRepository {
    fun getPrompt(promptType: PromptType): Flow<String>
    fun generateContent(model: String, parts: List<PartType>): Flow<Description>

    fun listDriveFiles(): Flow<Memories>
    fun deleteDriveItem(itemType: DriverItemType): Flow<Boolean>
    fun uploadDriveFile(save: SaveMemory): Flow<Memory>
}

