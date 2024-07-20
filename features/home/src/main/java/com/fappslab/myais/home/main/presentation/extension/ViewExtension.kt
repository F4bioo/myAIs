package com.fappslab.myais.home.main.presentation.extension

import androidx.activity.result.IntentSenderRequest
import com.fappslab.myais.arch.auth.AuthManager
import com.fappslab.myais.arch.camerax.model.CameraFlashType
import com.fappslab.myais.home.main.presentation.model.FlashType
import com.fappslab.myais.home.main.presentation.model.FlashType.Auto
import com.fappslab.myais.home.main.presentation.model.FlashType.Off
import com.fappslab.myais.home.main.presentation.model.FlashType.On

internal fun AuthManager.getAuthorizationClient(
    onLoggedOut: (IntentSenderRequest) -> Unit,
    onLoggedIn: () -> Unit
) {
    getAuthorizationClient(
        onSuccess = { authorizationResult ->
            if (authorizationResult.hasResolution()) {
                runCatching {
                    val intentSenderRequest = authorizationResult
                        .pendingIntent?.intentSender?.let {
                            IntentSenderRequest.Builder(it).build()
                        }
                    requireNotNull(intentSenderRequest)
                }.onFailure { cause ->
                    cause.printStackTrace()
                }.onSuccess { intentSenderRequest ->
                    onLoggedOut(intentSenderRequest)
                }
            } else {
                onLoggedIn()
            }
        },
        onFailure = { cause ->
            cause.printStackTrace()
        }
    )
}

internal fun FlashType.typeOf(): CameraFlashType {
    return when (this) {
        On -> CameraFlashType.On
        Off -> CameraFlashType.Off
        Auto -> CameraFlashType.Auto
    }
}
