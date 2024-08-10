package com.fappslab.myais.libraries.arch.simplepermission.model

sealed class PermissionStatus {
    data object Granted : PermissionStatus()
    data object Denied : PermissionStatus()
    data object AlwaysDenied : PermissionStatus()
}
