package com.alex.mediacenter

import android.app.Application
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.core.bus.RxBus
import com.alex.mediacenter.bus.AppEvent
import com.alex.mediacenter.feature.dummy.di.dummyModule
import com.alex.mediacenter.feature.player.di.playerModule
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.receiver.ConnectivityReceiver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MediacenterApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        setup()
    }

    // ----------------------------------------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppStart() {
        RxBus.publish(AppEvent(Lifecycle.Event.ON_START))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppStop() {
        RxBus.publish(AppEvent(Lifecycle.Event.ON_STOP))
    }

    // ----------------------------------------------------------------------------

    private fun setup() {
        setupConnectivityReceiver()
        setupMediaPlayer()
        setupTimber()
        setupKoin()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupConnectivityReceiver() {
        registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    private fun setupMediaPlayer() {
        MediaPlayer.init(this)
    }

    private fun setupTimber() {
        if (!BuildConfig.DEBUG) return

        Timber.plant(Timber.DebugTree())
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MediacenterApplication)
            modules(listOf(dummyModule, playerModule))
        }
    }
}