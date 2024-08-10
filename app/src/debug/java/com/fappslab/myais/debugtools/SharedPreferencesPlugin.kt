package com.fappslab.myais.debugtools

import android.content.Context
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin.SharedPreferencesDescriptor
import java.io.File

private const val SHARED_PREFS = "shared_prefs"

internal object SharedPreferencesPlugin {

    fun getDescriptors(context: Context): List<SharedPreferencesDescriptor> {
        return createDescriptors(getPrefsList(context))
    }

    private fun createDescriptors(prefsList: List<String>): List<SharedPreferencesDescriptor> {
        return prefsList.flatMap { prefsName ->
            listOf(SharedPreferencesDescriptor(prefsName, Context.MODE_PRIVATE))
        }
    }

    private fun getPrefsList(context: Context): List<String> {
        return runCatching {
            val prefsDir = File(context.applicationInfo.dataDir, SHARED_PREFS)
            val prefsList = prefsDir.listFiles()?.map { it.nameWithoutExtension }
            prefsList?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it }).orEmpty()
        }.getOrElse { emptyList() }
    }
}
