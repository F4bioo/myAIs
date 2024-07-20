package com.fappslab.myais.home.main.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fappslab.myais.home.R

internal enum class FailureType(
    @DrawableRes val illuRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
) {

    GenericError(
        illuRes = R.drawable.illu_error_generic,
        titleRes = R.string.failure_generic_title,
        messageRes = R.string.failure_generic_message,
    ),

    ConnectionError(
        illuRes = R.drawable.illu_error_connection,
        titleRes = R.string.failure_connection_title,
        messageRes = R.string.failure_connection_message,
    ),

    AnalyzeError(
        illuRes = R.drawable.illu_error_analyze,
        titleRes = R.string.failure_analyze_title,
        messageRes = R.string.failure_analyze_message,
    ),

    UploadError(
        illuRes = R.drawable.illu_error_connection,
        titleRes = R.string.failure_upload_title,
        messageRes = R.string.failure_upload_message,
    );
}
