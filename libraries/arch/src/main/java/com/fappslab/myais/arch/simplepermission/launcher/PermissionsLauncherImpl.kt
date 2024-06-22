package com.fappslab.myais.arch.simplepermission.launcher

import androidx.activity.result.ActivityResultLauncher

internal class PermissionsLauncherImpl(
    private val permissions: List<String>,
    private var resultLauncher: ActivityResultLauncher<Array<String>>
) : PermissionLauncher {

    override fun requestPermission() {
        resultLauncher.launch(permissions.toTypedArray())
    }
}
