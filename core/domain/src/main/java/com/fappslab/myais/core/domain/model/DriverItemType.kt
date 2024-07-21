package com.fappslab.myais.core.domain.model

sealed class DriverItemType {
    data object Folder : DriverItemType()
    data class File(val fileId: String) : DriverItemType()
}
