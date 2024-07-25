package com.fappslab.myais.features.home.main.presentation.extension

import androidx.activity.result.IntentSenderRequest
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Auto
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Off
import com.fappslab.myais.features.home.main.presentation.model.FlashType.On
import com.fappslab.myais.libraries.arch.auth.AuthManager
import com.fappslab.myais.libraries.arch.camerax.model.CameraFlashType

internal suspend fun AuthManager.getAuthorizationClient(
    onLoggedOut: (IntentSenderRequest) -> Unit,
    onLoggedIn: () -> Unit,
    onFailure: (Throwable) -> Unit
) {
    runCatching {
        getAuthorizationClient()
    }.onFailure {
        onFailure(it)
    }.onSuccess { authorizationResult ->
        if (authorizationResult.hasResolution()) {
            val intentSenderRequest = authorizationResult
                .pendingIntent?.intentSender?.let {
                    IntentSenderRequest.Builder(it).build()
                }
            intentSenderRequest?.let(onLoggedOut::invoke)
        } else onLoggedIn()
    }
}

internal fun FlashType.typeOf(): CameraFlashType {
    return when (this) {
        On -> CameraFlashType.On
        Off -> CameraFlashType.Off
        Auto -> CameraFlashType.Auto
    }
}
