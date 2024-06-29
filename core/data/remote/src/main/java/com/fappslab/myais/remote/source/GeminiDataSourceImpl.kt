package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.remote.api.GeminiService
import com.fappslab.myais.remote.model.mapper.toDescription
import com.fappslab.myais.remote.model.mapper.toDescriptionRequest

internal class GeminiDataSourceImpl(
    private val service: GeminiService
) : GeminiDataSource {

    override suspend fun generateContent(parts: List<PartType>): Description {
        val request = parts.toDescriptionRequest()
        return service.generateContent(request).toDescription()
    }
}
