package com.fappslab.myais.features.memories.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.features.memories.R
import com.fappslab.myais.features.memories.presentation.compose.component.EmptyScreenComponent
import com.fappslab.myais.features.memories.presentation.compose.component.FooterPagingComponent
import com.fappslab.myais.features.memories.presentation.compose.component.MemoryItemComponent
import com.fappslab.myais.features.memories.presentation.compose.component.TopBarComponent
import com.fappslab.myais.features.memories.presentation.viewmodel.FailureType
import com.fappslab.myais.features.memories.presentation.viewmodel.MemoriesViewIntent
import com.fappslab.myais.features.memories.presentation.viewmodel.MemoriesViewState
import com.fappslab.myais.libraries.arch.camerax.model.RatioType
import com.fappslab.myais.libraries.arch.extension.DateFormatType
import com.fappslab.myais.libraries.arch.extension.toFormatDate
import com.fappslab.myais.libraries.design.components.loading.PlutoLoadingDialog
import com.fappslab.myais.libraries.design.components.lorem.loremIpsum
import com.fappslab.myais.libraries.design.components.modal.PlutoModalComponent
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun MemoriesContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    state: MemoriesViewState,
    intent: (MemoriesViewIntent) -> Unit
) {
    val pagingItems = state.memories.collectAsLazyPagingItems()

    LaunchedEffect(pagingItems.loadState) {
        intent(MemoriesViewIntent.OnLoadStates(pagingItems))
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
            items(pagingItems.itemCount) { index ->
                pagingItems[index]?.let { memory ->
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
            item {
                FooterPagingComponent(
                    loadState = pagingItems.loadState,
                    onRetry = { pagingItems.retry() }
                )
            }
        }
        PlutoLoadingDialog(
            shouldShowDialog = state.shouldShowLoading,
            shouldShowLabel = true
        )
    }
    EmptyScreenComponent(
        shouldShowEmptyScreen = state.shouldShowEmptyScreen,
        onNavigationIconClicked = { intent(MemoriesViewIntent.OnBackClicked) }
    )
    ErrorModal(
        failureType = state.failureType,
        intent = intent
    )
}

@Composable
private fun ErrorModal(
    failureType: FailureType?,
    intent: (MemoriesViewIntent) -> Unit
) {

    failureType ?: return

    PlutoModalComponent(
        isSheetSwipeEnabled = false,
        shouldShow = true,
        titleRes = failureType.titleRes,
        messageRes = failureType.messageRes,
        image = {
            Image(
                modifier = Modifier.size(PlutoTheme.dimen.dp64),
                painter = painterResource(failureType.illuRes),
                colorFilter = ColorFilter.tint(PlutoTheme.colors.stealthGray),
                contentDescription = null
            )
        },
        onDismiss = {
            intent(MemoriesViewIntent.OnFailureModalClose)
        },
        primaryButton = {
            buttonTextRes = R.string.common_try_again
            onCLicked = {
                intent(MemoriesViewIntent.OnFailureModalRetry(failureType))
            }
        },
        secondaryButton = {
            buttonTextRes = R.string.common_close
            onCLicked = {
                intent(MemoriesViewIntent.OnFailureModalClose)
            }
        }
    )
}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
private fun MemoriesContentPreview() {
    val createdTime = "2024-07-05T05:10:12.168Z"
        .toFormatDate(DateFormatType.ISO_8601_TO_READABLE)
        .orEmpty()
    val owner = Owner(
        name = loremIpsum { 2 },
        email = "user@gmail.com",
        photoUrl = "photoUrl",
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
        thumbnailLink = "thumbnailLink",
        parentFolderId = "parentFolderId"
    )
    val state = MemoriesViewState(
        aspectRatio = RatioType.RATIO_16_9.toRatio(),
        memories = flowOf(PagingData.from(List(2) { memory })),
        shouldShowEmptyScreen = false,
        owner = owner,
    )
    PlutoTheme(
        darkTheme = false
    ) {
        MemoriesContent(
            contentPadding = PaddingValues(),
            state = state,
            intent = {}
        )
    }
}
