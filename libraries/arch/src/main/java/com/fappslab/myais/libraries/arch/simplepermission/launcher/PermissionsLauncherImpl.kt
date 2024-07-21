package com.fappslab.myais.libraries.arch.simplepermission.launcher

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus

internal class PermissionsLauncherImpl(
    private val context: Context,
    private val permissions: List<String>,
    private var resultLauncher: ActivityResultLauncher<Array<String>>
) : PermissionLauncher {

    override fun requestPermission() {
        resultLauncher.launch(permissions.toTypedArray())
    }

    override fun currentPermissionStatus(): PermissionStatus {
        val allGranted = permissions.all { permission ->
            val current = ContextCompat.checkSelfPermission(context, permission)
            current == PackageManager.PERMISSION_GRANTED
        }
        return if (allGranted) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied
        }
    }
}
