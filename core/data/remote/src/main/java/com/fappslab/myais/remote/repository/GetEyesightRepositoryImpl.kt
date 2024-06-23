package com.fappslab.myais.remote.repository

import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.model.PartType
import com.fappslab.myais.domain.repository.GetEyesightRepository
import com.fappslab.myais.remote.source.GetEyesightDataSource

internal class GetEyesightRepositoryImpl(
    private val dataSource: GetEyesightDataSource
) : GetEyesightRepository {
    override suspend fun getEyesight(parts: List<PartType>): Eyesight {
        return dataSource.getEyesightData(parts)
    }
}
