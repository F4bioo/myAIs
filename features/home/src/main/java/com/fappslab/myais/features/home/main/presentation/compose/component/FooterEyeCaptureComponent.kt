package com.fappslab.myais.features.home.main.presentation.compose.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.BlendModeColorFilterCompat.createBlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.fappslab.myais.libraries.design.accessibility.clearAndSetSemantics
import com.fappslab.myais.libraries.design.components.button.rememberClickAction
import com.fappslab.myais.libraries.design.components.footer.PlutoFooterLayout
import com.fappslab.myais.libraries.design.extension.clickable
import com.fappslab.myais.libraries.design.theme.PlutoTheme
import com.fappslab.myais.features.home.R
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Auto
import com.fappslab.myais.features.home.main.presentation.model.FlashType.Off
import com.fappslab.myais.features.home.main.presentation.model.FlashType.On
import com.fappslab.myais.features.home.main.presentation.model.MainStateType

private const val INITIAL_ROTATION = 0f
private const val ROTATION_INCREMENT_DEGREES = 180f
private const val ANIMATION_DURATION_ROTATION = 1000

@Composable
internal fun FooterEyeCaptureComponent(
    modifier: Modifier = Modifier,
    mainStateType: MainStateType,
    onCameraFlash: (FlashType) -> Unit,
    onCameraPhoto: () -> Unit,
    onCameraFlip: () -> Unit
) {
    val buttonBackground = MaterialTheme.colorScheme
        .surface.copy(PlutoTheme.opacity.frosted)

    PlutoFooterLayout {
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            ButtonFlashComponent(
                buttonBackground = buttonBackground,
                mainStateType = mainStateType,
                onCameraFlash = onCameraFlash
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp32))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(PlutoTheme.dimen.dp16))
                        .clickable(enabled = mainStateType != MainStateType.Analyze) {
                            onCameraPhoto()
                        },
                    shape = RoundedCornerShape(PlutoTheme.dimen.dp16),
                    colors = CardDefaults.cardColors(
                        containerColor = buttonBackground,
                        disabledContainerColor = buttonBackground.copy(alpha = 0.03f)
                    )
                ) {
                    ButtonPhotoComponent(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        mainStateType = mainStateType
                    )
                }
            }
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp32))
            ButtonFlipComponent(
                buttonBackground = buttonBackground,
                mainStateType = mainStateType,
                onCameraFlip = onCameraFlip
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
        }
    }
}

@Composable
private fun ButtonPhotoComponent(
    modifier: Modifier = Modifier,
    mainStateType: MainStateType,
) {
    val composition by rememberLottieComposition(RawRes(R.raw.eye_load))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = createBlendModeColorFilterCompat(
                PlutoTheme.colors.stealthGray.toArgb(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )

    Box(
        modifier = modifier.size(PlutoTheme.dimen.dp120),
        contentAlignment = Alignment.Center
    ) {
        when (mainStateType) {
            MainStateType.Camera -> EyeImageComponent(
                painter = painterResource(R.drawable.illu_eye_on_home),
            )

            MainStateType.Preview -> EyeImageComponent(
                painter = painterResource(R.drawable.illu_eye_off_home),
            )

            MainStateType.Analyze -> LottieAnimation(
                modifier = Modifier.matchParentSize(),
                iterations = LottieConstants.IterateForever,
                dynamicProperties = dynamicProperties,
                composition = composition
            )
        }
    }
}

@Composable
private fun EyeImageComponent(
    modifier: Modifier = Modifier,
    painter: Painter,
) {

    Image(
        modifier = modifier.padding(top = PlutoTheme.dimen.dp8),
        painter = painter,
        colorFilter = ColorFilter.tint(PlutoTheme.colors.stealthGray),
        contentDescription = null
    )
}

@Composable
private fun ButtonFlashComponent(
    buttonBackground: Color,
    mainStateType: MainStateType,
    onCameraFlash: (FlashType) -> Unit
) {
    var flashType by rememberSaveable { mutableStateOf(value = Off) }
    val debounceClick = rememberClickAction {
        val type = when (flashType) {
            Off -> On; On -> Auto; Auto -> Off
        }
        flashType = type
        onCameraFlash(type)
    }

    if (mainStateType == MainStateType.Camera) {
        IconButton(
            modifier = Modifier
                .clearAndSetSemantics {
                    this.liveRegion = LiveRegionMode.Polite
                    this.stateDescription = stringResource(flashType.stringResFlash())
                    this.role = Role.Button
                }
                .clip(CircleShape)
                .size(PlutoTheme.dimen.dp48)
                .layoutId(flashType)
                .background(buttonBackground),
            onClick = debounceClick,
        ) {
            Icon(
                painter = painterResource(flashType.iconRes),
                tint = PlutoTheme.colors.stealthGray,
                contentDescription = null,
            )
        }
    } else Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
}

private fun FlashType.stringResFlash(): Int {
    return when (this) {
        On -> R.string.home_desc_flash_on
        Off -> R.string.home_desc_flash_off
        Auto -> R.string.home_desc_flash_auto
    }
}

@Composable
private fun ButtonFlipComponent(
    buttonBackground: Color,
    mainStateType: MainStateType,
    onCameraFlip: () -> Unit
) {
    var backCameraSide by rememberSaveable { mutableStateOf(value = true) }
    var rotationDegrees by remember { mutableFloatStateOf(value = INITIAL_ROTATION) }
    val rotationAnim = remember { Animatable(initialValue = INITIAL_ROTATION) }
    val debounceClick = rememberClickAction {
        backCameraSide = !backCameraSide
        rotationDegrees -= ROTATION_INCREMENT_DEGREES
        onCameraFlip()
    }

    if (mainStateType == MainStateType.Camera) {
        IconButton(
            modifier = Modifier
                .clearAndSetSemantics {
                    this.liveRegion = LiveRegionMode.Polite
                    this.stateDescription = if (backCameraSide) {
                        stringResource(R.string.home_desc_showing_back_camera)
                    } else stringResource(R.string.home_desc_showing_front_camera)
                    this.role = Role.Button
                }
                .clip(CircleShape)
                .size(PlutoTheme.dimen.dp48)
                .layoutId(backCameraSide)
                .background(buttonBackground)
                .graphicsLayer { rotationZ = rotationAnim.value },
            onClick = debounceClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flip_camera),
                tint = PlutoTheme.colors.stealthGray,
                contentDescription = null
            )
        }
    } else Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
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

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
private fun FooterEyeCaptureComponentPreview() {
    PlutoTheme(
        darkTheme = true
    ) {
        FooterEyeCaptureComponent(
            mainStateType = MainStateType.Camera,
            onCameraFlash = {},
            onCameraPhoto = {},
            onCameraFlip = {}
        )
    }
}
