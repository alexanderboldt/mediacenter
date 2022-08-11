package com.alex.mediacenter.feature.player.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class State(initialPreview: PlayerPreview) {

    data class PlayerPreview(
        val showPlayButton: Boolean,
        val progress: Float,
        val duration: Float,
        val positionFormatted: String,
        val durationFormatted: String,
        val currentMediaItemIndex: Int,
        val mediaItems: List<MediaItem>?
    )

    data class MediaItem(
        val title: String
    )

    // ----------------------------------------------------------------------------

    var playerPreview by mutableStateOf(initialPreview)
}