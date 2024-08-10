package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class GetDriveOwnerUseCase(
    private val repository: MyAIsRepository
) {
    operator fun invoke(): Flow<Owner> {
        return repository.getOwner()
    }
}
