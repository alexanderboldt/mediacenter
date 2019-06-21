package com.alex.mediacenter

import android.app.Application
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.mediacenter.bus.AppEvent
import com.alex.mediacenter.bus.RxBus
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.receiver.ConnectivityReceiver
import com.squareup.leakcanary.LeakCanary

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

        // check if leakCanary could be initialized
        if (!setupLeakCanary()) {
            return
        }

        setupConnectivityReceiver()
        setupMediaPlayer()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupLeakCanary(): Boolean {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return false
        }

        LeakCanary.install(this)

        return true
    }

    private fun setupConnectivityReceiver() {
        registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    private fun setupMediaPlayer() {
        MediaPlayer.init(this)
    }
}