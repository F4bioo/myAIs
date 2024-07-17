package com.fappslab.myais.arch.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.fappslab.myais.arch.camerax.model.RatioType
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private const val COMPRESSION = 80

fun Uri.createBitmap(context: Context): Bitmap? {
    return runCatching {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(this)
        val result = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        result
    }.getOrNull()
}

fun Bitmap.cropToAspectRatio(ratioType: RatioType): Bitmap {
    if (ratioType.aspectX <= 0 || ratioType.aspectY <= 0) return this

    val originalRatio = width.toFloat() / height.toFloat()
    val targetRatio = ratioType.toRatio()

    if (originalRatio == targetRatio) return this

    val (newWidth, newHeight) = if (targetRatio > originalRatio) {
        width to (width / targetRatio).toInt()
    } else (height * targetRatio).toInt() to height

    val xOffset = ((width - newWidth) / 2).coerceAtLeast(0)
    val yOffset = ((height - newHeight) / 2).coerceAtLeast(0)

    return Bitmap.createBitmap(
        this,
        xOffset,
        yOffset,
        newWidth.coerceAtMost(width),
        newHeight.coerceAtMost(height)
    )
}

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

fun Bitmap.toByteArray(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 80
): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(compressFormat, quality, stream)
    return stream.toByteArray()
}

fun Bitmap.toFile(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 50,
    fileName: String,
    context: Context
): File {
    val file = File(context.cacheDir, fileName)
    file.createNewFile()

    val bos = ByteArrayOutputStream()
    compress(compressFormat, quality, bos)
    val bitmapData = bos.toByteArray()

    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()

    return file
}
