package com.alex.mediacenter.bus

import com.alex.mediacenter.player.MediaPlayer

data class MediaPlayerEvent(
    val state: MediaPlayer.State,
    val position: Long = 0,
    val duration: Long = 0,
    val title: String? = null,
    val imageUrl: String? = null)