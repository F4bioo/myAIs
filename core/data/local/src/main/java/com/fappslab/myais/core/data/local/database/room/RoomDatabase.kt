package com.fappslab.myais.core.data.local.database.room

import android.content.Context
import androidx.room.Room
import com.fappslab.myais.core.data.local.database.MyAIsDatabase
import com.fappslab.myais.core.data.local.database.SQLiteClient

internal class RoomDatabase(
    private val context: Context,
    private val name: String
) : SQLiteClient<MyAIsDatabase> {

    override fun create(): MyAIsDatabase {
        return Room.databaseBuilder(
            context,
            MyAIsDatabase::class.java,
            name
        ).build()
    }
}
