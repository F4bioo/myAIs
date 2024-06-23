package com.fappslab.myais.remote.network

interface HttpClient {
    fun <T> create(clazz: Class<T>): T
}
