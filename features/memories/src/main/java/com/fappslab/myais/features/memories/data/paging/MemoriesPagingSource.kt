package com.fappslab.myais.features.memories.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.repository.MyAIsRepository

internal class MemoriesPagingSource(
    private val pageSize: Int,
    private val repository: MyAIsRepository,
) : PagingSource<String, Memory>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Memory> {
        return runCatching {
            val pageToken = params.key
            val response = repository.listFiles(
                pageToken = pageToken,
                pageSize = pageSize
            )
            val nextKey = response.nextPageToken

            LoadResult.Page(
                data = response.list,
                nextKey = nextKey,
                prevKey = null,
            )

        }.getOrElse {
            LoadResult.Error(throwable = it)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Memory>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey ?: anchorPage?.prevKey
        }
    }
}
