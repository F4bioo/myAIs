package com.fappslab.myais.local.database

interface SQLiteClient<T> {
    fun create(): T
}
