package com.fappslab.myais.home.main.presentation.compose.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.design.accessibility.semantics
import com.fappslab.myais.design.components.lorem.loremIpsum
import com.fappslab.myais.design.components.shimmer.DefaultShimmerElement
import com.fappslab.myais.design.components.shimmer.ShimmerLayout
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.R
import com.fappslab.myais.home.main.presentation.model.MainStateType

@Composable
internal fun BodyDescriptionComponent(
    modifier: Modifier = Modifier,
    description: String,
    mainStateType: MainStateType,
    uploadDescription: Int,
    onUploadClicked: () -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        RotatingImage(
            modifier = Modifier.size(PlutoTheme.dimen.dp28),
            painter = painterResource(R.drawable.ic_gemini_sparkle),
            rotate = mainStateType == MainStateType.Analyze,
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
        if (mainStateType == MainStateType.Analyze) {
            ShimmerComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PlutoTheme.dimen.dp8)
            )
        } else TextDescription(
            description = description,
            mainStateType = mainStateType,
            contentDescription = uploadDescription,
            onUploadClicked = onUploadClicked
        )
    }
}

@Composable
private fun RotatingImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    rotate: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotationTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotationAnimation"
    )

    val rotationAngle = if (rotate) rotation else 0f
    Image(
        modifier = modifier.rotate(rotationAngle),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun ShimmerComponent(
    modifier: Modifier = Modifier
) {

    ShimmerLayout(
        modifier = modifier.padding(end = PlutoTheme.dimen.dp16)
    ) {
        DefaultShimmerElement()
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp12))
        DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp32))
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp12))
        DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp16))
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp12))
        DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp64))
    }
}

@Composable
private fun TextDescription(
    modifier: Modifier = Modifier,
    description: String,
    mainStateType: MainStateType,
    contentDescription: Int,
    onUploadClicked: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = PlutoTheme.dimen.dp4,
                    end = PlutoTheme.dimen.dp8,
                ),
            text = description,
            style = PlutoTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        UploadButtonComponent(
            mainStateType = mainStateType,
            contentDescription = contentDescription,
            onUploadClicked = onUploadClicked
        )
    }
}

@Composable
private fun UploadButtonComponent(
    modifier: Modifier = Modifier,
    mainStateType: MainStateType,
    contentDescription: Int,
    onUploadClicked: () -> Unit
) {

    Box(
        modifier = modifier.offset(x = -PlutoTheme.dimen.dp14)
    ) {
        if (mainStateType == MainStateType.Preview) {
            IconButton(
                modifier = Modifier
                    .size(PlutoTheme.dimen.dp48)
                    .semantics(
                        mergeDescendants = true
                    ) {
                        liveRegion = LiveRegionMode.Assertive
                        onClick(
                            label = stringResource(R.string.home_desc_save_to_drive),
                            action = null
                        )
                    },
                onClick = onUploadClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cloud_upload),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(contentDescription),
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun BodyDescriptionComponentPreview() {
    Box(
        modifier = Modifier.padding(PlutoTheme.dimen.dp16)
    ) {
        BodyDescriptionComponent(
            description = loremIpsum { 30 },
            mainStateType = MainStateType.Preview,
            uploadDescription = R.string.home_desc_save_memory,
            onUploadClicked = {}
        )
    }
}
