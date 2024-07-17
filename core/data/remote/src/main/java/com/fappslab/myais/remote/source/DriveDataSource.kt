package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.model.Memories
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.SaveMemory
import kotlinx.coroutines.flow.Flow

internal interface DriveDataSource {
    fun listFiles(): Flow<Memories>
    fun deleteItem(itemType: DriverItemType): Flow<Boolean>
    fun uploadFile(save: SaveMemory): Flow<Memory>
}
