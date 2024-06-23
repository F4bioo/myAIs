package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.remote.api.GeminiService
import com.fappslab.myais.remote.model.mapper.toEyesight
import com.fappslab.myais.remote.model.mapper.toEyesightRequest

internal class GetEyesightDataSourceImpl(
    private val service: GeminiService
) : GetEyesightDataSource {
    override suspend fun getEyesightData(parts: List<PartType>): Eyesight {
        return service.generateContent(parts.toEyesightRequest()).toEyesight()
    }
}
