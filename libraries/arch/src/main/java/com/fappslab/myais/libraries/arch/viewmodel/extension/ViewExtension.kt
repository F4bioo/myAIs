package com.fappslab.myais.libraries.arch.viewmodel.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    crossinline eventBlock: suspend (T) -> Unit
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
