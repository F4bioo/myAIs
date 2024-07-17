package com.fappslab.myais.arch.simplepermission.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.fappslab.myais.arch.simplepermission.launcher.PermissionLauncher
import com.fappslab.myais.arch.simplepermission.launcher.PermissionsLauncherImpl
import com.fappslab.myais.arch.simplepermission.model.PermissionStatus

fun ComponentActivity.permissionLauncher(
    vararg permissions: String,
    resultBLock: (PermissionStatus) -> Unit
): PermissionLauncher {

    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val isAnyPermissionDenied = permissionsMap.any { (_, isGranted) -> isGranted.not() }
        val alwaysDenied = permissions.any { permission ->
            isAnyPermissionDenied && shouldShowRequestPermissionRationale(permission).not()
        }

        when {
            alwaysDenied -> resultBLock(PermissionStatus.AlwaysDenied)
            isAnyPermissionDenied -> resultBLock(PermissionStatus.Denied)
            else -> resultBLock(PermissionStatus.Granted)
        }
    }

    return PermissionsLauncherImpl(this, permissions.toList(), launcher)
}

fun Fragment.permissionLauncher(
    vararg permissions: String,
    resultBLock: (PermissionStatus) -> Unit
): PermissionLauncher {

    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val isAnyPermissionDenied = permissionsMap.any { (_, isGranted) -> isGranted.not() }
        val alwaysDenied = permissions.any { permission ->
            isAnyPermissionDenied && shouldShowRequestPermissionRationale(permission).not()
        }

        when {
            alwaysDenied -> resultBLock(PermissionStatus.AlwaysDenied)
            isAnyPermissionDenied -> resultBLock(PermissionStatus.Denied)
            else -> resultBLock(PermissionStatus.Granted)
        }
    }

    return PermissionsLauncherImpl(requireContext(), permissions.toList(), launcher)
}


fun Context.openApplicationSettings() {
    Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    }.also(::startActivity)
}

fun Context.openLinkInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    startActivity(intent)
}
