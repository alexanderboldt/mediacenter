package com.alex.mediacenter.feature.player.model

data class UiModelPlayerPreview(
    val showPlayButton: Boolean,
    val title: String,
    val progress: Float,
    val coverUrl: String?,
    val position: String,
    val duration: String
)