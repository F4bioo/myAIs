package com.fappslab.myais.features.memories.presentation.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.fappslab.myais.libraries.arch.camerax.model.RatioType
import com.fappslab.myais.libraries.arch.extension.DateFormatType
import com.fappslab.myais.libraries.arch.extension.toFormatDate
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.components.lorem.loremIpsum
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.features.memories.R

@Composable
internal fun MemoryItemComponent(
    modifier: Modifier = Modifier,
    aspectRatio: Float,
    memory: Memory,
    onDeleteClicked: (String) -> Unit,
    onDownloadClicked: (Memory) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.clearAndSetSemantics {
                this.contentDescription = stringResource(
                    R.string.memories_desc_memory_complete,
                    memory.description,
                    memory.createdTime
                )
            }
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .clip(RoundedCornerShape(PlutoTheme.radius.large))
                    .background(MaterialTheme.colorScheme.onBackground.copy(PlutoTheme.opacity.frosted)),
                model = memory.thumbnailLink,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            Column(
                modifier = Modifier.padding(horizontal = PlutoTheme.dimen.dp16)
            ) {
                Text(
                    text = memory.description,
                    style = PlutoTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
                Text(
                    text = memory.createdTime,
                    style = PlutoTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                    color = PlutoTheme.text.colorPlaceholder,
                )
            }
        }
        Row(
            modifier = Modifier
        ) {
            IconButton(
                modifier = Modifier.size(PlutoTheme.dimen.dp48),
                onClick = { onDeleteClicked(memory.id) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    tint = PlutoTheme.text.colorPlaceholder,
                    contentDescription = stringResource(R.string.memories_delete_memory),
                )
            }
            IconButton(
                modifier = Modifier.size(PlutoTheme.dimen.dp48),
                onClick = { onDownloadClicked(memory) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    tint = PlutoTheme.text.colorPlaceholder,
                    contentDescription = stringResource(R.string.memories_download_memory),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoryItemComponentPreview() {
    val createdTime = "2024-07-05T05:10:12.168Z"
        .toFormatDate(DateFormatType.ISO_8601_TO_READABLE)
        .orEmpty()
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
    PlutoTheme {
        MemoryItemComponent(
            aspectRatio = RatioType.RATIO_16_9.toRatio(),
            memory = memory,
            onDeleteClicked = {},
            onDownloadClicked = {}
        )
    }
}
