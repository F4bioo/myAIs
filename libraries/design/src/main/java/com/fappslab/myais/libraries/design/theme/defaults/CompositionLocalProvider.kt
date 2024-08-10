package com.fappslab.myais.libraries.design.theme.defaults

import androidx.compose.runtime.staticCompositionLocalOf
import com.fappslab.myais.libraries.design.theme.defaults.dark.DesignLanguageDark
import com.fappslab.myais.libraries.design.theme.defaults.dark.PlutoColorsDark
import com.fappslab.myais.libraries.design.theme.defaults.light.DesignLanguageLight
import com.fappslab.myais.libraries.design.theme.defaults.light.PlutoColorsLight
import com.fappslab.myais.libraries.design.theme.tokens.PlutoDimens
import com.fappslab.myais.libraries.design.theme.tokens.PlutoElevation
import com.fappslab.myais.libraries.design.theme.tokens.PlutoFontSizing
import com.fappslab.myais.libraries.design.theme.tokens.PlutoFontStyle
import com.fappslab.myais.libraries.design.theme.tokens.PlutoOpacity
import com.fappslab.myais.libraries.design.theme.tokens.PlutoRadius
import com.fappslab.myais.libraries.design.theme.tokens.PlutoStroke
import com.fappslab.myais.libraries.design.theme.tokens.PlutoTypography

val LocalPlutoDimens = staticCompositionLocalOf {
    PlutoDimens
}

val LocalPlutoElevation = staticCompositionLocalOf {
    PlutoElevation
}

val LocalPlutoFontSizing = staticCompositionLocalOf {
    PlutoFontSizing
}

val LocalPlutoFontFontStyle = staticCompositionLocalOf {
    PlutoFontStyle
}

val LocalPlutoOpacity = staticCompositionLocalOf {
    PlutoOpacity
}

val LocalPlutoRadius = staticCompositionLocalOf {
    PlutoRadius
}

val LocalPlutoStroke = staticCompositionLocalOf {
    PlutoStroke
}

val LocalPlutoTextDark = staticCompositionLocalOf {
    DesignLanguageDark.text
}

val LocalPlutoTextLight = staticCompositionLocalOf {
    DesignLanguageLight.text
}

val LocalPlutoTypography = staticCompositionLocalOf {
    PlutoTypography
}

val LocalPlutoColorsLight = staticCompositionLocalOf {
    PlutoColorsLight
}

val LocalPlutoColorsDark = staticCompositionLocalOf {
    PlutoColorsDark
}
