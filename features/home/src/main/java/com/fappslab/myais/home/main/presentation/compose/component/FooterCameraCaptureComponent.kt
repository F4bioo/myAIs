package com.fappslab.myais.home.main.presentation.compose.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.fappslab.myais.design.components.button.rememberClickAction
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.R

private const val INITIAL_SCALE = 1f
private const val TARGET_SCALE_DOWN = 0.8f
private const val ANIMATION_DURATION_SCALE_DOWN = 150
private const val ANIMATION_DURATION_SCALE_UP = 300

private const val INITIAL_ROTATION = 0f
private const val ROTATION_INCREMENT_DEGREES = 180f
private const val ANIMATION_DURATION_ROTATION = 1000

@Composable
internal fun FooterCameraCaptureComponent(
    modifier: Modifier = Modifier,
    @DrawableRes flashIconRes: Int,
    onCameraFlash: () -> Unit,
    onCameraClicked: () -> Unit,
    onCameraFlipped: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonFlash(
                flashIconRes = flashIconRes,
                onCameraFlash = onCameraFlash
            )
            ButtonCapture(
                modifier = Modifier
                    .padding(top = PlutoTheme.dimen.dp16)
                    .weight(1f),
                onCameraClicked = onCameraClicked
            )
            ButtonFlip(
                onCameraFlipped = onCameraFlipped
            )
        }
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp100))
    }
}

@Composable
private fun ButtonFlash(
    @DrawableRes flashIconRes: Int,
    onCameraFlash: () -> Unit
) {
    val debounceClick = rememberClickAction(action = onCameraFlash)

    IconButton(
        modifier = Modifier
            .padding(start = PlutoTheme.dimen.dp16)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.05f)),
        onClick = debounceClick,
    ) {
        Icon(
            painter = painterResource(flashIconRes),
            tint = PlutoTheme.text.colorPlaceholder,
            contentDescription = null,
        )
    }
}

@Composable
private fun ButtonCapture(
    modifier: Modifier,
    onCameraClicked: () -> Unit
) {
    val scale = remember { Animatable(initialValue = INITIAL_SCALE) }
    val cameraClickState = remember { mutableStateOf(value = false) }
    val debounceClick = rememberClickAction(action = onCameraClicked)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(PlutoTheme.dimen.dp80)
                .scale(scale.value)
                .clip(CircleShape)
                .clickable {
                    cameraClickState.value = true
                    debounceClick()
                },
            painter = painterResource(R.drawable.ic_camera_button),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.35f)),
            contentDescription = null
        )
    }
    if (cameraClickState.value) {
        LaunchedEffect(cameraClickState) {
            scale.animateTo(
                targetValue = TARGET_SCALE_DOWN,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE_DOWN,
                    easing = FastOutSlowInEasing
                )
            )
            scale.animateTo(
                targetValue = INITIAL_SCALE,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE_UP,
                    easing = FastOutSlowInEasing
                )
            )
            cameraClickState.value = false
        }
    }
}

@Composable
private fun ButtonFlip(
    onCameraFlipped: () -> Unit
) {
    var rotationDegrees by remember { mutableFloatStateOf(value = INITIAL_ROTATION) }
    val rotationAnim = remember { Animatable(initialValue = INITIAL_ROTATION) }
    val debounceClick = rememberClickAction {
        rotationDegrees -= ROTATION_INCREMENT_DEGREES
        onCameraFlipped()
    }

    IconButton(
        modifier = Modifier
            .padding(end = PlutoTheme.dimen.dp16)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.05f))
            .graphicsLayer { rotationZ = rotationAnim.value },
        onClick = debounceClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_flip_camera),
            tint = PlutoTheme.text.colorPlaceholder,
            contentDescription = "Flip Camera",
        )
    }
    LaunchedEffect(rotationDegrees) {
        rotationAnim.animateTo(
            targetValue = rotationDegrees,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION_ROTATION,
                easing = FastOutSlowInEasing
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FooterCameraCaptureComponentPreview() {
    Column(
        modifier = Modifier.padding(PlutoTheme.dimen.dp16),
        verticalArrangement = Arrangement.Center
    ) {
        FooterCameraCaptureComponent(
            flashIconRes = R.drawable.ic_flash_auto,
            onCameraFlash = {},
            onCameraClicked = {},
            onCameraFlipped = {}
        )
    }
}
