package com.fappslab.myais.arch.di

import android.app.DownloadManager
import android.content.Context
import com.fappslab.myais.arch.auth.AuthManager
import com.fappslab.myais.arch.auth.AuthManagerImpl
import com.fappslab.myais.arch.downloader.Downloader
import com.fappslab.myais.arch.downloader.DownloaderImpl
import com.fappslab.myais.arch.koin.koinshot.KoinShot
import com.fappslab.myais.arch.navigation.FeatureRoute
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val FEATURE_ROUTES_QUALIFIER = "feature_routes"

internal class ArchModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single<AuthManager> {
            AuthManagerImpl(androidApplication())
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
