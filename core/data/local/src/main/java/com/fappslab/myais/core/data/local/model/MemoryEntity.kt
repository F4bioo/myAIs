package com.fappslab.myais.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory")
internal data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,
    @ColumnInfo(name = "text")
    val description: String,
    @ColumnInfo(name = "data")
    val imageId: String,
    @ColumnInfo(name = "date")
    val date: Long
)
