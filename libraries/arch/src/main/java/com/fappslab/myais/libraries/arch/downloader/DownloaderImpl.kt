package com.fappslab.myais.libraries.arch.downloader

import android.app.DownloadManager
import android.os.Environment
import androidx.core.net.toUri
import com.fappslab.myais.libraries.arch.downloader.Downloader.DownloaderParams

internal class DownloaderImpl(
    private val downloadManager: DownloadManager
) : Downloader {

    override fun downloadFile(params: DownloaderParams): Long {
        val request = DownloadManager.Request(params.fileUrl.toUri())
            .setTitle(params.fileName)
            .setMimeType(params.mimeType)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, params.fileName)

        return downloadManager.enqueue(request)
    }
}
