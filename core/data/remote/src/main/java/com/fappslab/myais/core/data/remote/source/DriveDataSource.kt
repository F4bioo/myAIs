package com.fappslab.myais.core.data.remote.source

import com.fappslab.myais.core.domain.model.DriverItemType
import com.fappslab.myais.core.domain.model.Memories
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.core.domain.model.SaveMemory
import kotlinx.coroutines.flow.Flow

internal interface DriveDataSource {
    fun getOwner(): Flow<Owner>
    fun uploadFile(save: SaveMemory): Flow<Memory>
    fun deleteItem(itemType: DriverItemType): Flow<Boolean>
    suspend fun listFiles(pageToken: String?, pageSize: Int): Memories
}
