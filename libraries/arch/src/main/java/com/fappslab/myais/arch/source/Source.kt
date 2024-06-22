package com.fappslab.myais.arch.source

interface Source<T> {
    fun create(): T
}
