package com.fappslab.myais.arch.kofee

import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any> fakeBean(): T {
    return fakeBean(T::class)
}

fun <T : Any> fakeBean(kClass: KClass<T>): T {
    if (kClass.java.isInterface) {
        return createInterfaceFake(kClass)
    }

    kClass.primaryConstructor?.let { constructor ->
        constructor.isAccessible = true
        val parameters = constructor.parameters.associateWith { parameter ->
            getDefault(parameter.type.classifier as? KClass<*>)
        }
        return constructor.callBy(parameters)
    }

    return kClass.createInstance()
}

internal fun getDefault(kClass: KClass<*>?): Any? {
    return kClass?.let {
        when {
            it.constructors.isNotEmpty() -> fakeBean(it)
            else -> null
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> createInterfaceFake(kClass: KClass<T>): T {
    val proxy = Proxy.newProxyInstance(
        kClass.java.classLoader,
        arrayOf(kClass.java)
    ) { _, _, _ -> null }

    return proxy as T
}
