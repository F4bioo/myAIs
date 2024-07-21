package com.fappslab.myais.core.data.remote.source

import com.fappslab.myais.core.domain.model.DriverItemType
import com.fappslab.myais.core.domain.model.Memories
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.SaveMemory
import kotlinx.coroutines.flow.Flow

internal interface DriveDataSource {
    fun listFiles(): Flow<Memories>
    fun deleteItem(itemType: DriverItemType): Flow<Boolean>
    fun uploadFile(save: SaveMemory): Flow<Memory>
}
