package com.fappslab.myais.core.data.remote.network.exception.extension

import com.fappslab.myais.core.data.remote.model.DescriptionErrorResponse
import com.fappslab.myais.core.data.remote.network.exception.model.HttpThrowable
import com.fappslab.myais.core.data.remote.network.exception.model.InternetThrowable
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val UNEXPECTED_ERROR_MESSAGE = "Unexpected error, please try again."

private fun HttpException.getCause(errorBody: String?): Throwable =
    getErrorResponse(errorBody)?.error?.let {
        HttpThrowable(it.code, it.message, this)
    } ?: HttpThrowable(message = UNEXPECTED_ERROR_MESSAGE, throwable = this)

private fun getErrorResponse(errorBody: String?): DescriptionErrorResponse? =
    Gson().fromJson(errorBody, DescriptionErrorResponse::class.java)

private fun HttpException.parseError(): Throwable =
    getCause(errorBody = response()?.errorBody()?.string())

private fun Throwable.toThrowable(): Throwable =
    when (this) {
        is HttpException -> parseError()
        is UnknownHostException,
        is SocketTimeoutException,
        is IOException -> InternetThrowable()

        else -> this
    }

internal fun <T> Flow<T>.parseHttpError(): Flow<T> {
    return catch { cause ->
        throw cause.toThrowable()
    }
}
