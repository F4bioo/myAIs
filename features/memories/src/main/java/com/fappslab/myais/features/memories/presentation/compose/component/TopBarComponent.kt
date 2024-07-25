package com.fappslab.myais.features.memories.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.core.domain.model.Owner
import com.fappslab.myais.features.memories.R
import com.fappslab.myais.libraries.arch.extension.isNotNull
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.components.image.PlutoAsyncImage
import com.fappslab.myais.libraries.design.extension.clickable
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarComponent(
    modifier: Modifier = Modifier,
    owner: Owner?,
    onNavigationIconClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(
                modifier = Modifier.size(PlutoTheme.dimen.dp48),
                onClick = onNavigationIconClicked,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = PlutoTheme.text.colorPlaceholder,
                    contentDescription = stringResource(R.string.memories_navigate_to_previous_screen),
                )
            }
        },
        title = {
            InfoComponent(
                name = owner?.name
            ) { userName ->
                Text(
                    modifier = Modifier.clearAndSetSemantics {
                        this.contentDescription = stringResource(
                            R.string.memories_desc_screen_header,
                            userName
                        )
                    },
                    text = userName,
                    style = PlutoTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Row(
                    modifier = Modifier
                        .clearAndSetSemantics {
                            this.contentDescription = stringResource(
                                R.string.memories_logout
                            )
                            this.role = Role.Button
                        }
                        .clickable { onLogoutClicked() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.clearAndSetSemantics {
                            contentDescription = String()
                        },
                        text = stringResource(R.string.memories_subtitle),
                        style = PlutoTheme.typography.bodyLarge,
                        color = PlutoTheme.text.colorPlaceholder
                    )
                    Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
                    Icon(
                        modifier = Modifier.size(PlutoTheme.dimen.dp16),
                        painter = painterResource(R.drawable.ic_logout),
                        tint = PlutoTheme.text.colorPlaceholder,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            PhotoComponent(
                name = owner?.name,
                photoUrl = owner?.photoUrl,
                modifier = Modifier.size(PlutoTheme.dimen.dp48)
            )
        },
    )
}

@Composable
private fun InfoComponent(
    modifier: Modifier = Modifier,
    name: String?,
    content: @Composable ColumnScope.(String) -> Unit
) {

    if (name.isNotNull()) Column(
        modifier = modifier.fillMaxWidth(),
        content = { content(name) }
    )
}

@Composable
private fun PhotoComponent(
    modifier: Modifier = Modifier,
    name: String?,
    photoUrl: String?
) {

    PlutoAsyncImage(
        modifier = modifier
            .size(PlutoTheme.dimen.dp48)
            .clip(CircleShape)
            .border(
                width = PlutoTheme.stroke.line,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        model = photoUrl,
    ) { isLoading, painter ->
        if (!isLoading) {
            Image(
                painter = painter,
                contentDescription = stringResource(
                    id = R.string.memories_desc_user_photo,
                    name.orEmpty()
                )
            )
        }
    }
}

@Preview
@Composable
private fun PlutoTopBarComponentPreview() {
    val owner = Owner(
        name = "User Name",
        photoUrl = "photoUrl",
        email = "user@gmail.com"
    )
    TopBarComponent(
        onNavigationIconClicked = {},
        onLogoutClicked = {},
        owner = owner
    )
}
