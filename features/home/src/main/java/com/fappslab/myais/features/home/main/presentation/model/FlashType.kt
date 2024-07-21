package com.fappslab.myais.features.home.main.presentation.model

import androidx.annotation.DrawableRes
import com.fappslab.myais.features.home.R

internal enum class FlashType(@DrawableRes val iconRes: Int) {
    On(iconRes = R.drawable.ic_flash_on),
    Off(iconRes = R.drawable.ic_flash_off),
    Auto(iconRes = R.drawable.ic_flash_auto);
}
