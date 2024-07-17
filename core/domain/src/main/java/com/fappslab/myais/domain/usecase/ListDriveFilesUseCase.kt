package com.fappslab.myais.domain.usecase

import com.fappslab.myais.domain.repository.MyAIsRepository

class ListDriveFilesUseCase(
    private val repository: MyAIsRepository
) {
    operator fun invoke() = repository.listDriveFiles()
}
