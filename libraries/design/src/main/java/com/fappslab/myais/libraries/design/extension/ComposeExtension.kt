package com.fappslab.myais.libraries.design.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fappslab.myais.libraries.design.components.button.ClickType
import com.fappslab.myais.libraries.design.components.button.rememberClickAction
import com.fappslab.myais.libraries.design.theme.tokens.PlutoRadius

@Composable
fun Modifier.clickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    clickType: ClickType = ClickType.Debounce,
    onClicked: () -> Unit
): Modifier {
    return this
        .clip(RoundedCornerShape(PlutoRadius.medium))
        .clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            onClick = rememberClickAction(clickType = clickType, action = onClicked)
        )
}

@Composable
fun Float.toDp(): Dp {
    val density = LocalDensity.current.density
    return (this / density).dp
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current.density
    return value * density
}
