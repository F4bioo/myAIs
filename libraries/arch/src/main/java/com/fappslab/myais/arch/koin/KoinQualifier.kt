package com.fappslab.myais.arch.koin

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

interface KoinQualifier : Qualifier {

    override val value: QualifierValue
        get() = this::class.java.name
}
