package com.fappslab.myais.core.data.local.database

interface SQLiteClient<T> {
    fun create(): T
}
