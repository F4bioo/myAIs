package com.fappslab.myais.memories.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.arch.camerax.model.RatioType
import com.fappslab.myais.arch.extension.DateFormatType
import com.fappslab.myais.arch.extension.toFormatDate
import com.fappslab.myais.design.components.loading.PlutoLoadingDialog
import com.fappslab.myais.design.components.lorem.loremIpsum
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.domain.model.Memory
import com.fappslab.myais.domain.model.Owner
import com.fappslab.myais.memories.presentation.compose.component.EmptyScreenComponent
import com.fappslab.myais.memories.presentation.compose.component.MemoryItemComponent
import com.fappslab.myais.memories.presentation.compose.component.TopBarComponent
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewIntent
import com.fappslab.myais.memories.presentation.viewmodel.MemoriesViewState

@Composable
internal fun MemoriesContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    state: MemoriesViewState,
    intent: (MemoriesViewIntent) -> Unit
) {

    LaunchedEffect(Unit) {
        intent(MemoriesViewIntent.OnInitView)
    }
    Box(
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
        ) {
            item {
                TopBarComponent(
                    modifier = Modifier.padding(
                        end = PlutoTheme.dimen.dp12
                    ),
                    owner = state.owner,
                    onNavigationIconClicked = {
                        intent(MemoriesViewIntent.OnBackClicked)
                    },
                    onLogoutClicked = {
                        intent(MemoriesViewIntent.OnLogoutClicked)
                    }
                )
                Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            }
            items(state.memories.orEmpty()) { memory ->
                MemoryItemComponent(
                    modifier = Modifier.padding(horizontal = PlutoTheme.dimen.dp16),
                    aspectRatio = state.aspectRatio,
                    memory = memory,
                    onDeleteClicked = {
                        intent(MemoriesViewIntent.OnShowDeleteDialog(memory.id))
                    },
                    onDownloadClicked = {
                        intent(MemoriesViewIntent.OnShowDownloadDialog(memory = it))
                    },
                )
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = PlutoTheme.dimen.dp16)
                )
            }
        }
        PlutoLoadingDialog(
            shouldShowDialog = state.shouldShowLoading,
            shouldShowLabel = true
        )
    }
    EmptyScreenComponent(
        memories = state.memories,
        onNavigationIconClicked = {
            intent(MemoriesViewIntent.OnBackClicked)
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MemoriesContentPreview() {
    val createdTime = "2024-07-05T05:10:12.168Z"
        .toFormatDate(DateFormatType.ISO_8601_TO_READABLE)
        .orEmpty()
    val owner = Owner(
        name = loremIpsum { 2 },
        photoUrl = "photoUrl",
        parentFolderId = "parentFolderId"
    )
    val memory = Memory(
        id = "id",
        size = 123,
        fileName = "myAIs_memory_1720156208520.jpg",
        mimeType = "image/jpeg",
        description = loremIpsum { 25 },
        createdTime = createdTime,
        webViewLink = "webViewLink",
        webContentLink = "webContentLink",
        thumbnailLink = "thumbnailLink"
    )
    val state = MemoriesViewState(
        aspectRatio = RatioType.RATIO_16_9.toRatio(),
        owner = owner,
        memories = List(2) { memory }
    )
    PlutoTheme {
        MemoriesContent(
            contentPadding = PaddingValues(),
            state = state,
            intent = {}
        )
    }
}
