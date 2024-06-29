package com.fappslab.myais.arch.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@Keep
fun Bitmap.toBase64(quality: Int = 80): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

@Keep
fun Context.fileFromDrawable(@DrawableRes drawableId: Int, quality: Int = 80): File {
    val bitmap = BitmapFactory.decodeResource(resources, drawableId)
    val file = File(cacheDir, "myAIs_memory_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}
