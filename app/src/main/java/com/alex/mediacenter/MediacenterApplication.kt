package com.alex.mediacenter

import android.app.Application
import com.alex.mediacenter.feature.base.di.baseModule
import com.alex.mediacenter.feature.player.di.playerModule
import com.alex.mediacenter.feature.selector.di.selectorModule
import com.alex.mediacenter.player.di.mediaPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MediaCenterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    // ----------------------------------------------------------------------------

    private fun setupKoin() {
        startKoin {
            androidContext(this@MediaCenterApplication)
            modules(
                listOf(
                    selectorModule,
                    playerModule,
                    baseModule,
                    mediaPlayerModule
                )
            )
        }
    }
}