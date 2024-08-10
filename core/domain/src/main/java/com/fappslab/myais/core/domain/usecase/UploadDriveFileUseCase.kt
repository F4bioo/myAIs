package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.SaveMemory
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class UploadDriveFileUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(save: SaveMemory): Flow<Memory> {
        return repository.uploadDriveFile(save)
    }
}
