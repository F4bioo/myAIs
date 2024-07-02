package com.fappslab.myais.home.agreement.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
internal fun HeaderComponent(
    modifier: Modifier = Modifier,
    isGrantedPermission: Boolean
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.eye_load))
    var isPlaying by remember { mutableStateOf(value = false) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying
    )

    LaunchedEffect(isGrantedPermission) {
        isPlaying = isGrantedPermission
    }

    LaunchedEffect(progress, isGrantedPermission) {
        if (progress == 1f && isGrantedPermission) {
            isPlaying = false
            val randomDelay = Random
                .nextLong(from = 0, until = 3000)
            delay(randomDelay)
            isPlaying = true
        }
    }

    Column(
        modifier = modifier.semantics(mergeDescendants = true) {
            heading()
            contentDescription = "Header of the myAIs app with the logo of a eye."
        }
    ) {
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.app_name),
            style = PlutoTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp4))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(PlutoTheme.dimen.dp120),
            contentAlignment = Alignment.Center
        ) {
            if (isGrantedPermission) {
                LottieAnimation(
                    modifier = Modifier,
                    progress = { progress },
                    composition = composition,
                )
            } else {
                Image(
                    modifier = Modifier.padding(top = PlutoTheme.dimen.dp16),
                    painter = painterResource(id = R.drawable.illu_eye_off),
                    colorFilter = ColorFilter.tint(Color(0xFF3C4853)),
                    contentDescription = null
                )
            }
        }
    }
}


@Preview
@Composable
private fun HeaderComponentPreview() {
    HeaderComponent(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        isGrantedPermission = true
    )
}
