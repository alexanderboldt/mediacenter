package com.alex.mediacenter.bus

import androidx.lifecycle.Lifecycle
import com.alex.mediacenter.player.MediaPlayer

// the whole app is in background/foreground
data class AppEvent(val event: Lifecycle.Event)

// the BottomSheet is opened/closed
data class BottomSheetExpandEvent(var isExpanded: Boolean)
data class BottomSheetOffsetEvent(var offset: Float?)

// the app has internet-connection or not
data class ConnectivityEvent(var connected: Boolean)

// the state of the player
data class MediaPlayerEvent(
    val state: MediaPlayer.State,
    val position: Long = 0,
    val duration: Long = 0,
    val title: String? = null,
    val imageUrl: String? = null)