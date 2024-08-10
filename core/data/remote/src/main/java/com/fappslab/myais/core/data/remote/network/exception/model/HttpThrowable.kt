package com.fappslab.myais.core.data.remote.network.exception.model

class HttpThrowable(
    val code: Int? = null,
    message: String,
    throwable: Throwable
) : Throwable(message, throwable)
