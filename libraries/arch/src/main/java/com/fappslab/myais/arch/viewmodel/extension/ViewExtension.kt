package com.fappslab.myais.arch.viewmodel.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.observeAsEvents(
    lifecycleOwner: LifecycleOwner,
    crossinline eventBlock: (T) -> Unit
) {

    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeAsEvents.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { eventBlock(it) }
        }
    }
}

@Composable
inline fun <reified T> Flow<T>.observeAsEvents(
    crossinline eventBlock: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = this, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeAsEvents.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { eventBlock(it) }
        }
    }
}

/*@Composable
inline fun <reified T> Flow<T>.observeAsEvents(
    crossinline eventBlock: @Composable (T) -> Unit
) {
    val eventState = remember { mutableStateOf<T?>(value = null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    DisposableEffect(key1 = this, key2 = lifecycleOwner) {
        val job = scope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observeAsEvents.flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { event ->
                    eventState.value = event
                }
            }
        }

        onDispose {
            job.cancel()
        }
    }

    eventState.value?.let { event ->
        eventBlock(event)
    }
}*/
