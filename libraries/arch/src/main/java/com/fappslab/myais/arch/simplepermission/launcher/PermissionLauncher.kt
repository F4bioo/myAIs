package com.fappslab.myais.arch.simplepermission.launcher

import com.fappslab.myais.arch.simplepermission.model.PermissionStatus

interface PermissionLauncher {
    fun requestPermission()
    fun currentPermissionStatus(): PermissionStatus
}
