package com.fappslab.myais.arch.viewmodel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel as LifecycleViewModel

abstract class ViewModel<State, Effect>(initialState: State) : LifecycleViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.CONFLATED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    val viewState: State
        get() = state.value

    protected fun onState(stateBlock: (State) -> State) {
        _state.update { stateBlock(it) }
    }

    protected fun onEffect(actionBlock: () -> Effect) {
        _effect.trySend(actionBlock())
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}

interface ViewIntent<Intent> {
    fun onViewIntent(intent: Intent)
}
