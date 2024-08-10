package com.fappslab.myais.features.memories.presentation.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.features.memories.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class MemoriesViewState(
    val aspectRatio: Float,
    val owner: Owner? = null,
    val fileId: String? = null,
    val errorMessage: String? = null,
    val failureType: FailureType? = null,
    val shouldShowLoading: Boolean = false,
    val shouldShowEmptyScreen: Boolean = true,
    val memories: Flow<PagingData<Memory>> = emptyFlow()
) {

    fun getDriveOwnerFailureState() = copy(
        shouldShowLoading = false,
        shouldShowEmptyScreen = true,
        failureType = FailureType.ListDriveFiles
    )
}

internal enum class FailureType(
    @DrawableRes val illuRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int
) {
    ListDriveFiles(
        illuRes = R.drawable.illu_error_generic,
        titleRes = R.string.memories_list_drive_files_error_title,
        messageRes = R.string.memories_list_drive_files_error_message
    ),
    DeleteDriveItem(
        illuRes = R.drawable.illu_error_generic,
        titleRes = R.string.memories_delete_drive_item_error_title,
        messageRes = R.string.memories_delete_drive_item_error_message
    ),
}
