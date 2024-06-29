package com.fappslab.myais.domain.repository

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.DriveFile
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.model.SaveMemory

interface MyAIsRepository {
    suspend fun generateContent(parts: List<PartType>): Description

    suspend fun listDriveFiles(): List<DriveFile>
    suspend fun deleteDriveItem(itemType: DriverItemType): Boolean
    suspend fun uploadDriveFile(save: SaveMemory): DriveFile
}
