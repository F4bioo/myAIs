package com.fappslab.myais.libraries.arch.di

import android.app.DownloadManager
import android.content.Context
import com.fappslab.myais.libraries.arch.downloader.Downloader
import com.fappslab.myais.libraries.arch.downloader.DownloaderImpl
import com.fappslab.myais.libraries.arch.koin.koinshot.KoinShot
import com.fappslab.myais.libraries.arch.navigation.FeatureRoute
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val FEATURE_ROUTES_QUALIFIER = "feature_routes"

internal class ArchModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single<com.fappslab.myais.libraries.arch.auth.AuthManager> {
            com.fappslab.myais.libraries.arch.auth.AuthManagerImpl(androidApplication())
        }
    }

    override val additionalModule = module {
        single(named(FEATURE_ROUTES_QUALIFIER)) {
            getAll<FeatureRoute>()
        }

        factory<Downloader> {
            DownloaderImpl(
                downloadManager = get<Context>()
                    .getSystemService(DownloadManager::class.java)
            )
        }
    }
}
