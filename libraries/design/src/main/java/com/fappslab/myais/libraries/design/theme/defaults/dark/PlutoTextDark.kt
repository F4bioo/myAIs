package com.fappslab.myais.libraries.design.theme.defaults.dark

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.fappslab.myais.libraries.design.theme.tokens.PlutoText
import com.fappslab.myais.libraries.design.theme.tokens.onBackgroundDark
import com.fappslab.myais.libraries.design.theme.tokens.primaryDark
import com.fappslab.myais.libraries.design.theme.tokens.secondaryDark

@Immutable
object PlutoTextDark : PlutoText {
    override val colorPrimary: Color = primaryDark
    override val colorSecondary: Color = secondaryDark
    override val colorPlaceholder: Color = onBackgroundDark.copy(alpha = 0.7f)
}
