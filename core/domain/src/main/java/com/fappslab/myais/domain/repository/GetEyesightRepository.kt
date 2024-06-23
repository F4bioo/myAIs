package com.fappslab.myais.domain.repository

import com.fappslab.myais.domain.model.Eyesight
import com.fappslab.myais.domain.model.PartType

interface GetEyesightRepository {
    suspend fun getEyesight(parts: List<PartType>): Eyesight
}
