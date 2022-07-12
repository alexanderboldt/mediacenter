package com.alex.mediacenter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.alex.mediacenter.player.MediaPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Receives events from headsets.
 */
class HeadsetReceiver : BroadcastReceiver(), KoinComponent {

    private val mediaPlayer: MediaPlayer by inject()

    // ----------------------------------------------------------------------------

    companion object {
        fun register(context: Context) {
            context.registerReceiver(HeadsetReceiver(), IntentFilter(Intent.ACTION_HEADSET_PLUG))
        }
    }

    // ----------------------------------------------------------------------------

    override fun onReceive(context: Context, intent: Intent) {
        // headset was unplugged
        if (intent.getIntExtra("state", -1) == 0) {
            mediaPlayer.pause()
        }
    }
}