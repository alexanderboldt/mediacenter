package com.alex.mediacenter

import android.app.Application
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.core.bus.RxBus
import com.alex.mediacenter.bus.AppEvent
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.receiver.ConnectivityReceiver
import leakcanary.LeakCanary
import leakcanary.LeakSentry
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
        setupLeakCanary()
        setupConnectivityReceiver()
        setupMediaPlayer()
        setupTimber()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupLeakCanary() {
        if (!BuildConfig.DEBUG) return

        LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = false)
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = true)
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
}