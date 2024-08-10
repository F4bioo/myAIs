package com.fappslab.myais.features.memories.data.paging

import androidx.paging.PagingData
import com.fappslab.myais.core.domain.model.Memory
import kotlinx.coroutines.flow.Flow

internal interface MemoriesPagingDataFactory {
    fun create(pageSize: Int): Flow<PagingData<Memory>>
}
