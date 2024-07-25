package com.fappslab.myais.features.memories.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

internal class MemoriesPagingDataFactoryImpl(
    private val repository: MyAIsRepository,
) : MemoriesPagingDataFactory {

    override fun create(pageSize: Int): Flow<PagingData<Memory>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { MemoriesPagingSource(pageSize, repository) }
        ).flow
    }
}
