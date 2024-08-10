package com.fappslab.myais.libraries.design.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics

@Composable
fun Modifier.semantics(
    mergeDescendants: Boolean = false,
    properties: @Composable SemanticsPropertyReceiver.() -> Unit
): Modifier {
    val receiver = remember { SemanticsPropertyReceiverImpl() }

    receiver.apply {
        properties()
    }

    return this.semantics(mergeDescendants) {
        receiver.applyTo(this)
    }
}

@Composable
fun Modifier.clearAndSetSemantics(
    properties: @Composable SemanticsPropertyReceiver.() -> Unit
): Modifier {
    val receiver = remember { SemanticsPropertyReceiverImpl() }

    receiver.apply {
        properties()
    }

    return this.clearAndSetSemantics {
        receiver.applyTo(this)
    }
}

private class SemanticsPropertyReceiverImpl : SemanticsPropertyReceiver {
    private val properties = mutableMapOf<SemanticsPropertyKey<Any?>, Any?>()

    override fun <T> set(key: SemanticsPropertyKey<T>, value: T) {
        @Suppress("UNCHECKED_CAST")
        properties[key as SemanticsPropertyKey<Any?>] = value
    }

    fun applyTo(receiver: SemanticsPropertyReceiver) {
        for ((key, value) in properties) {
            receiver[key] = value
        }
    }
}
