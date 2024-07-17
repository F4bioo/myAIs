package com.fappslab.myais.memories.presentation.viewmodel

import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.Owner

internal data class MemoriesViewState(
    val aspectRatio: Float,
    val owner: Owner? = null,
    val memories: List<Memory>? = null,
    val shouldShowLoading: Boolean = false,
)
