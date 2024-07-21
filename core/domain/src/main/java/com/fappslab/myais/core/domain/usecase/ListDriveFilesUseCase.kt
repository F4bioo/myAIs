package com.fappslab.myais.core.domain.usecase

import com.fappslab.myais.core.domain.repository.MyAIsRepository

class ListDriveFilesUseCase(
    private val repository: MyAIsRepository
) {
    operator fun invoke() = repository.listDriveFiles()
}
