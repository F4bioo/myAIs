package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.DriveFile
import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.SaveMemory

internal interface DriveDataSource {
    suspend fun listFiles(): List<DriveFile>
    suspend fun deleteItem(itemType: DriverItemType): Boolean
    suspend fun uploadFile(save: SaveMemory): DriveFile
}
