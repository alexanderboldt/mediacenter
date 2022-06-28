package com.alex.mediacenter.feature.player.model

data class UiModelPlayerPreview(
    val showPlayButton: Boolean,
    val title: String,
    val progress: Float,
    val duration: Float,
    val coverUrl: String?,
    val positionFormatted: String,
    val durationFormatted: String
)