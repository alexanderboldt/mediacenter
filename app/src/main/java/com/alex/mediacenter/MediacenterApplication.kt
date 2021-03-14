package com.alex.mediacenter

import android.app.Application
import com.alex.mediacenter.feature.dummy.di.dummyModule
import com.alex.mediacenter.feature.player.di.playerModule
import com.alex.mediacenter.player.di.mediaPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MediacenterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setup()
    }

    // ----------------------------------------------------------------------------

    private fun setup() {
        setupTimber()
        setupKoin()
    }

    private fun setupTimber() {
        if (!BuildConfig.DEBUG) return

        Timber.plant(Timber.DebugTree())
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MediacenterApplication)
            modules(listOf(
                    dummyModule,
                    playerModule,
                    mediaPlayerModule))
        }
    }
}