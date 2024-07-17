package com.fappslab.myais.arch.downloader


interface Downloader {
    fun downloadFile(params: DownloaderParams): Long

    data class DownloaderParams(
        val fileUrl: String,
        val fileName: String,
        val mimeType: String
    )
}
