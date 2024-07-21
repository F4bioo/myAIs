package com.fappslab.myais.libraries.design.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@Composable
fun ShimmerLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier, content = content)
}

@Composable
private fun shimmerAnimation(
    durationMillis: Int = 3000
): Brush {
    val shimmerColors = listOf(
        Color(0xFF9D5263),
        Color(0xFFA65565),
        Color(0xFF6C4257)
    )
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "Shimmer"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.background(shimmerAnimation())
    )
}

@Composable
fun DefaultShimmerElement(
    modifier: Modifier = Modifier
) {

    ShimmerBox(
        modifier = modifier
            .fillMaxWidth()
            .height(PlutoTheme.dimen.dp16)
            .clip(RoundedCornerShape(PlutoTheme.radius.large))
    )
}

@Preview(showBackground = true)
@Composable
private fun ShimmerLayoutPreview() {

    Column {
        ShimmerLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            DefaultShimmerElement()
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp32))
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp16))
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            DefaultShimmerElement(Modifier.padding(end = PlutoTheme.dimen.dp64))
        }
    }
}
