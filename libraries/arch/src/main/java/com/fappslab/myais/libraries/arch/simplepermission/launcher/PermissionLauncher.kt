package com.fappslab.myais.libraries.arch.simplepermission.launcher

import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus

interface PermissionLauncher {
    fun requestPermission()
    fun currentPermissionStatus(): PermissionStatus
}
