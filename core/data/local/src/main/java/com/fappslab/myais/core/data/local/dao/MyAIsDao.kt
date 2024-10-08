package com.fappslab.myais.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.myais.core.data.local.model.MemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MyAIsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Query("DELETE FROM memory WHERE _id = :id")
    suspend fun deleteMemory(id: Int)

    @Query("SELECT * FROM memory")
    fun selectMemories(): Flow<List<MemoryEntity>>
}
