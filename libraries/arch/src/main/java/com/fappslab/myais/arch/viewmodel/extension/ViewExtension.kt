package com.fappslab.myais.arch.viewmodel.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fappslab.myais.arch.viewmodel.ViewModel
import kotlinx.coroutines.launch

inline fun <reified State, reified Effect> LifecycleOwner.onViewState(
    viewModel: ViewModel<State, Effect>,
    crossinline state: (State) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect { state(it) }
        }
    }
}

inline fun <reified State, reified Effect> LifecycleOwner.onViewEffect(
    viewModel: ViewModel<State, Effect>,
    crossinline action: (Effect) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.action.collect { action(it) }
        }
    }
}
