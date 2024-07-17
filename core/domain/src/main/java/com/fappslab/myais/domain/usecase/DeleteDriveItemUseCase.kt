package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.model.DriverItemType
import com.fappslab.myais.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class DeleteDriveItemUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(itemType: DriverItemType): Flow<Boolean> {
        return repository.deleteDriveItem(itemType)
    }
}
