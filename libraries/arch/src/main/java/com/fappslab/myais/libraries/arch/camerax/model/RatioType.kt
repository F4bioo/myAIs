package com.fappslab.myais.libraries.arch.camerax.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
enum class RatioType(val aspectX: Int, val aspectY: Int) : Parcelable {
    RATIO_1_1(aspectX = 1, aspectY = 1),
    RATIO_3_4(aspectX = 3, aspectY = 4),
    RATIO_16_9(aspectX = 16, aspectY = 9),
    RATIO_9_16(aspectX = 9, aspectY = 16);

    fun toRatio(): Float {
        return aspectX.toFloat() / aspectY.toFloat()
    }
}
