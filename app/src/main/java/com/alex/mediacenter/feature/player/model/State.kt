package com.alex.mediacenter.feature.player.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class State(initialPreview: PlayerPreview) {

    data class PlayerPreview(
        val showPlayButton: Boolean,
        val title: String,
        val progress: Float,
        val duration: Float,
        val coverUrl: String?,
        val positionFormatted: String,
        val durationFormatted: String
    )

    // ----------------------------------------------------------------------------

    var playerPreview by mutableStateOf(initialPreview)
}