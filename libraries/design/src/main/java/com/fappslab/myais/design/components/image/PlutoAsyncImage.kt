package com.fappslab.myais.design.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage

@Composable
fun PlutoAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    content: @Composable (Boolean, AsyncImagePainter) -> Unit
) {

    SubcomposeAsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = null
    ) {

        val state = painter.state
        content(
            state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error,
            painter
        )
    }
}
