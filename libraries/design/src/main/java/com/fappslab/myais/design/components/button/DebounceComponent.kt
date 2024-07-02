package com.fappslab.myais.design.components.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DEFAULT_DEBOUNCE_TIME_MILLS = 500L

@Composable
fun rememberClickAction(
    debounceTime: Long = DEFAULT_DEBOUNCE_TIME_MILLS,
    clickType: ClickType = ClickType.Debounce,
    action: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(value = 0L) }
    val scope = rememberCoroutineScope()

    return when (clickType) {
        ClickType.Debounce -> {
            {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > debounceTime) {
                    action()
                    lastClickTime = currentTime

                } else scope.launch {
                    delay(debounceTime - (currentTime - lastClickTime))
                    lastClickTime = System.currentTimeMillis()
                }
            }
        }

        ClickType.Instant -> {
            {
                action()
            }
        }
    }
}

enum class ClickType {
    Debounce, Instant
}
