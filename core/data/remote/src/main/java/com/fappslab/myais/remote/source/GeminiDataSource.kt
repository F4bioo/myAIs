package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Description
import com.fappslab.myais.domain.model.PartType

internal interface GeminiDataSource {
    suspend fun generateContent(parts: List<PartType>): Description
}
