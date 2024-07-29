package com.fappslab.myais.libraries.arch.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class AuthManagerImpl(private val context: Context) : AuthManager {

    override suspend fun getAuthorizationClient(): AuthorizationResult {
        return Identity.getAuthorizationClient(context)
            .authorize(authorizationRequest())
            .await()
    }

    override suspend fun logout(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        try {
            val request = ClearCredentialStateRequest()
            CredentialManager.create(context)
                .clearCredentialState(request)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } catch (cause: Throwable) {
            cause.printStackTrace()
            withContext(Dispatchers.Main) {
                onFailure(cause)
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        val authorizationResult = getAuthorizationClient()
        return authorizationResult.accessToken
    }

    private fun authorizationRequest(): AuthorizationRequest {
        val requestedScopes = listOf(
            Scope(DriveScopes.DRIVE_FILE),
        )
        return AuthorizationRequest.Builder()
            .setRequestedScopes(requestedScopes)
            .build()
    }
}
