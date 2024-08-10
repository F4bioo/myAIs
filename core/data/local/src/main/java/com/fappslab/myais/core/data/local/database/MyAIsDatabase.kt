package com.fappslab.myais.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.myais.core.data.local.dao.MyAIsDao
import com.fappslab.myais.core.data.local.model.MemoryEntity

@Database(entities = [MemoryEntity::class], version = 1, exportSchema = false)
internal abstract class MyAIsDatabase : RoomDatabase() {
    abstract fun myAIsDao(): MyAIsDao
}
