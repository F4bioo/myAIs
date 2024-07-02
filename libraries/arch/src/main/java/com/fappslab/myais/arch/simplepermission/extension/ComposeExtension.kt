package com.fappslab.myais.arch.simplepermission.extension

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.fappslab.myais.arch.simplepermission.launcher.PermissionLauncher
import com.fappslab.myais.arch.simplepermission.launcher.PermissionsLauncherImpl
import com.fappslab.myais.arch.simplepermission.model.PermissionStatus

@Composable
fun rememberPermissionLauncher(
    vararg permissions: String,
    resultBlock: (PermissionStatus) -> Unit
): PermissionLauncher {
    val context = LocalContext.current
    val currentResultBlock by rememberUpdatedState(resultBlock)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val isAnyPermissionDenied = permissionsMap.any { (_, isGranted) -> !isGranted }
        val alwaysDenied = permissions.any { permission ->
            isAnyPermissionDenied && !shouldShowRequestPermissionRationale(context, permission)
        }

        when {
            alwaysDenied -> currentResultBlock(PermissionStatus.AlwaysDenied)
            isAnyPermissionDenied -> currentResultBlock(PermissionStatus.Denied)
            else -> currentResultBlock(PermissionStatus.Granted)
        }
    }

    return remember(launcher) {
        PermissionsLauncherImpl(context, permissions.toList(), launcher)
    }
}

private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return if (context is ComponentActivity) {
        ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
    } else false
}
