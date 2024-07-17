package com.fappslab.myais.arch.auth

import com.google.android.gms.auth.api.identity.AuthorizationResult

interface AuthManager {
    fun getAuthorizationClient(
        onSuccess: (AuthorizationResult) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    suspend fun logout(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    )

    suspend fun getAccessToken(): String?
}
