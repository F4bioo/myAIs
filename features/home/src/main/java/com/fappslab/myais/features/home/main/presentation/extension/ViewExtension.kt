package com.fappslab.myais.features.home.main.presentation.extension

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.os.Parcel
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Auto
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Off
import com.fappslab.myais.features.home.main.presentation.model.FlashType.On
import com.fappslab.myais.features.home.main.presentation.viewmodel.HomeViewIntent
import com.fappslab.myais.libraries.arch.auth.AuthManager
import com.fappslab.myais.libraries.arch.camerax.model.CameraFlashType
import com.fappslab.myais.libraries.arch.extension.orFalse
import com.google.android.gms.common.api.Status

internal suspend fun AuthManager.getAuthorizationClient(
    onLoggedOut: (IntentSenderRequest) -> Unit,
    onLoggedIn: () -> Unit,
    onFailure: (Throwable) -> Unit
) {
    runCatching {
        getAuthorizationClient()
    }.onFailure { cause ->
        onFailure(cause)
    }.onSuccess { authorizationResult ->
        if (authorizationResult.hasResolution()) {
            val intentSenderRequest = authorizationResult
                .pendingIntent?.intentSender?.let { intentSender ->
                    IntentSenderRequest.Builder(intentSender).build()
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

internal fun ActivityResult.handleLauncherResult(
    intent: (HomeViewIntent) -> Unit
) {
    when (resultCode) {
        RESULT_OK -> intent(HomeViewIntent.OnGoogleAuthResulOk)

        // The OAuth 2.0 API can be inconsistent in handling user cancellations,
        // sometimes returning RESULT_CANCELED. In such cases, it is necessary to translate
        // this result into an appropriate error code.
        RESULT_CANCELED -> googleAuthResulCanceled(intent::invoke)
    }
}

internal fun ActivityResult.googleAuthResulCanceled(
    intent: (HomeViewIntent) -> Unit
) {
    data?.extras?.run {
        if (containsKey("status").orFalse()) {
            val statusParcel = Parcel.obtain()
            try {
                val byteArray = getByteArray("status")
                byteArray?.let {
                    statusParcel.unmarshall(it, 0, it.size)
                    statusParcel.setDataPosition(0)
                    val status = Status.CREATOR.createFromParcel(statusParcel)
                    intent(HomeViewIntent.OnGoogleAuthResulCanceled(status))
                }
            } finally {
                statusParcel.recycle()
            }
        }
    }
}
