package com.fappslab.myais.arch.auth.compose

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.fappslab.myais.arch.auth.AuthManager
import org.koin.compose.koinInject

@Composable
fun AuthManagerComponent(
    authManager: AuthManager = koinInject(),
    onGrantedAccess: () -> Unit,
    onFailedAccess: (Throwable) -> Unit
) {
    val signInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) result.data?.let {
            onGrantedAccess()
        }
    }

    LaunchedEffect(Unit) {
        authManager.getAuthorizationClient(
            onSuccess = { authorizationResult ->
                if (authorizationResult.hasResolution()) {
                    runCatching {
                        val intent = authorizationResult.pendingIntent?.intentSender?.let {
                            IntentSenderRequest.Builder(intentSender = it).build()
                        }
                        requireNotNull(intent)
                    }.onFailure { cause ->
                        onFailedAccess(cause)
                    }.onSuccess { intent ->
                        signInLauncher.launch(intent)
                    }
                } else onGrantedAccess()
            },
            onFailure = onFailedAccess
        )
    }
}
