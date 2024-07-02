package com.fappslab.myais.arch.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> T?.isNull(): Boolean {
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun <T> T?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T> Any?.isTypeOf(): Boolean {
    contract {
        returns(true) implies (this@isTypeOf is T)
    }
    return this is T
}
