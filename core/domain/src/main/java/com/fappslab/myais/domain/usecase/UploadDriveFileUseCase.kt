package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.SaveMemory
import com.fappslab.myais.domain.repository.MyAIsRepository
import kotlinx.coroutines.flow.Flow

class UploadDriveFileUseCase(
    private val repository: MyAIsRepository
) {

    operator fun invoke(save: SaveMemory): Flow<Memory> {
        return repository.uploadDriveFile(save)
    }
}
