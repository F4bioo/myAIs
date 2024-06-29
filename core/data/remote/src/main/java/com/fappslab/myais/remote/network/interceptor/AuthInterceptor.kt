package com.fappslab.myais.remote.network.interceptor

import android.content.Context
import com.fappslab.myais.remote.BuildConfig
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { getAccessToken(context) }
        //println("<L> ======================")
        //println("<L> accessToken: $accessToken")
        //println("<L> ======================")
        val requestBuilder = chain.request().newBuilder()
        val newBuilder = accessToken?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } ?: requestBuilder
        return chain.proceed(newBuilder.build())
    }

    private suspend fun getAccessToken(context: Context): String? {
        val requestedScopes = listOf(
            Scope(DriveScopes.DRIVE_FILE),
            Scope(DriveScopes.DRIVE_APPDATA)
        )
        val authorizationRequest = AuthorizationRequest.Builder()
            .setRequestedScopes(requestedScopes)
            .requestOfflineAccess(BuildConfig.CLIENT_ID_WEB)
            .build()
        val authorizationResult = Identity
            .getAuthorizationClient(context)
            .authorize(authorizationRequest)
            .await()

        return authorizationResult.accessToken
    }
}
