package com.fappslab.myais.features.memories.presentation.viewmodel

import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner

internal data class MemoriesViewState(
    val aspectRatio: Float,
    val owner: Owner? = null,
    val memories: List<Memory>? = null,
    val shouldShowLoading: Boolean = false,
)
