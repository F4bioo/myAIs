package com.fappslab.myais.home.main.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
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
import com.fappslab.myais.design.components.footer.PlutoFooterLayout
import com.fappslab.myais.design.extension.clickable
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.R
import com.fappslab.myais.home.main.presentation.model.MainStateType

@Composable
internal fun FooterEyeCaptureComponent(
    modifier: Modifier = Modifier,
    mainStateType: MainStateType,
    isButtonEyeEnabled: Boolean,
    onEyeButtonClicked: () -> Unit
) {

    PlutoFooterLayout {
        Card(
            modifier = modifier
                .sizeIn(minWidth = PlutoTheme.dimen.dp200)
                .align(Alignment.CenterHorizontally)
                .padding(top = PlutoTheme.dimen.dp8)
                .background(
                    color = Color.Black.copy(PlutoTheme.opacity.faint),
                    shape = RoundedCornerShape(PlutoTheme.dimen.dp16)
                )
                .clickable(enabled = isButtonEyeEnabled) {
                    onEyeButtonClicked()
                }
        ) {
            EyeButtonContentComponent(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                mainStateType = mainStateType
            )
        }
    }
}

@Composable
private fun EyeButtonContentComponent(
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

@Preview(showBackground = true)
@Composable
private fun FooterEyeCaptureComponentPreview() {
    FooterEyeCaptureComponent(
        mainStateType = MainStateType.Camera,
        isButtonEyeEnabled = true,
        onEyeButtonClicked = {}
    )
}
