package com.fappslab.myais.features.memories.presentation.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.fappslab.myais.features.memories.R
import com.fappslab.myais.libraries.design.components.button.ButtonType
import com.fappslab.myais.libraries.design.components.button.PlutoButtonComponent
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
internal fun FooterPagingComponent(
    shouldShowLoading: Boolean = false,
    loadState: CombinedLoadStates,
    onRetry: () -> Unit
) {
    when {
        shouldShowLoading && loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
            LoadingView()
        }

        loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
            ErrorView(
                message = loadState.handleError()?.message,
            ) {
                onRetry()
            }
        }
    }
}

@Composable
private fun LoadingView(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = Color.Red
        )
    }
}

@Composable
private fun ErrorView(
    modifier: Modifier = Modifier,
    message: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PlutoTheme.dimen.dp16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message ?: stringResource(R.string.memories_load_items_error_message),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = PlutoTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
            color = PlutoTheme.text.colorPlaceholder,
            textAlign = TextAlign.Center
        )
        PlutoButtonComponent(
            text = stringResource(id = R.string.common_try_again),
            buttonType = ButtonType.Tertiary,
            onClick = onClick
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
    }
}

private fun CombinedLoadStates.handleError(): Throwable? {
    val loadStates = listOf(
        source.refresh, source.prepend, source.append, // PagingSource LoadState
        refresh, append, prepend // RemoteMediator LoadState
    )

    val errorState = loadStates
        .filterIsInstance<LoadState.Error>()
        .firstOrNull()

    return errorState?.error
}

@Preview(showBackground = true)
@Composable
private fun FooterPagingComponentPreview() {
    val refreshLoading = LoadState.Loading
    val refreshError = LoadState.Error(
        Throwable("Unable to solve host \"www.google.com.br\". No address associated with hostname")
    )
    val loadState = CombinedLoadStates(
        refresh = refreshError,
        prepend = LoadState.NotLoading(endOfPaginationReached = true),
        append = LoadState.NotLoading(endOfPaginationReached = true),
        source = LoadStates(
            refresh = refreshError,
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true)
        ),
        mediator = null
    )
    PlutoTheme {
        FooterPagingComponent(
            shouldShowLoading = true,
            loadState = loadState,
            onRetry = {}
        )
    }
}
