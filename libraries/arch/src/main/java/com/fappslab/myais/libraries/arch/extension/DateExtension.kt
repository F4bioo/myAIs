package com.fappslab.myais.libraries.arch.extension

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

enum class DateFormatType(val formatMap: Pair<String, String>) {
    ISO_8601_TO_DD_MM_YY_HH_MM("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" to "dd/MM/yy HH:mm"),
    ISO_8601_TO_READABLE("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" to "d 'de' MMMM 'de' yyyy, HH'h'mm")
}

fun String?.toFormatDate(formatType: DateFormatType): String? {
    if (this.isNullOrEmpty()) return null

    return runCatching {
        val (input, output) = formatType.formatMap
        val inputFormat = SimpleDateFormat(input, Locale.ROOT)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
        val outputFormat = SimpleDateFormat(output, Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let(outputFormat::format)
    }.getOrNull()
}
