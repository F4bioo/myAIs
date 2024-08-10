package com.fappslab.myais.libraries.arch.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

class UriReader(
    private val context: Context
) {

    fun readAsBitmap(uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }
}
