package com.fappslab.myais.home.main.presentation.extension

import androidx.activity.result.IntentSenderRequest
import com.fappslab.myais.arch.auth.AuthManager

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
