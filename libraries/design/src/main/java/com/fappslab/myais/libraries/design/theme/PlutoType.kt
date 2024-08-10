package com.fappslab.myais.libraries.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.fappslab.myais.libraries.design.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val robotoFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
        weight = FontWeight.Light
    ),
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
        weight = FontWeight.Medium
    ),
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
        weight = FontWeight.Bold
    )
)

// Default Material 3 typography values
val baseline = Typography()
