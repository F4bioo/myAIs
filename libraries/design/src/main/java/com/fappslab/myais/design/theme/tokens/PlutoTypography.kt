package com.fappslab.myais.design.theme.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight
import com.fappslab.myais.design.theme.baseline
import com.fappslab.myais.design.theme.robotoFontFamily

@Immutable
object PlutoTypography {
    val displayLarge = baseline.displayLarge.copy(fontFamily = robotoFontFamily)
    val displayMedium = baseline.displayMedium.copy(fontFamily = robotoFontFamily)
    val displaySmall = baseline.displaySmall.copy(fontFamily = robotoFontFamily)
    val headlineLarge = baseline.headlineLarge.copy(fontFamily = robotoFontFamily)
    val headlineMedium = baseline.headlineMedium.copy(fontFamily = robotoFontFamily)
    val headlineSmall = baseline.headlineSmall.copy(fontFamily = robotoFontFamily)
    val titleLarge = baseline.titleLarge.copy(fontFamily = robotoFontFamily)
    val titleMedium = baseline.titleMedium.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium)
    val titleSmall = baseline.titleSmall.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium)
    val bodyLarge = baseline.bodyLarge.copy(fontFamily = robotoFontFamily)
    val bodyMedium = baseline.bodyMedium.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Light)
    val bodySmall = baseline.bodySmall.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Light)
    val labelLarge = baseline.labelLarge.copy(fontFamily = robotoFontFamily)
    val labelMedium = baseline.labelMedium.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium)
    val labelSmall = baseline.labelSmall.copy(fontFamily = robotoFontFamily, fontWeight = FontWeight.Light)
}
