package com.fappslab.myais.libraries.arch.auth

import com.google.android.gms.auth.api.identity.AuthorizationResult

interface AuthManager {
    suspend fun getAuthorizationClient(): AuthorizationResult
    suspend fun logout(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit)
    suspend fun getAccessToken(): String?
}
