package com.fappslab.myais.libraries.arch.downloader


interface Downloader {
    fun downloadFile(params: DownloaderParams): Long

    data class DownloaderParams(
        val fileUrl: String,
        val fileName: String,
        val mimeType: String
    )
}
