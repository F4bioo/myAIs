package com.fappslab.myais.libraries.design.theme.defaults.light

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.fappslab.myais.libraries.design.theme.tokens.PlutoColors
import com.fappslab.myais.libraries.design.theme.tokens.blueLinkLight
import com.fappslab.myais.libraries.design.theme.tokens.stealthGrayLight

@Immutable
object PlutoColorsLight : PlutoColors {
    override val stealthGray: Color = stealthGrayLight
    override val blueLink: Color = blueLinkLight
}
