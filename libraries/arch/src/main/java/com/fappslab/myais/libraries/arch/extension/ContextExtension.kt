package com.fappslab.myais.libraries.arch.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}
