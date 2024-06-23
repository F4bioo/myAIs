package com.fappslab.myais.remote.source

import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.model.PartType

internal interface GetEyesightDataSource {
    suspend fun getEyesightData(parts: List<PartType>): Eyesight
}
