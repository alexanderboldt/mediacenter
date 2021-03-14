package com.alex.mediacenter.feature.player.model

data class UiModelPlayerState(
    val isPreviewPlayButtonVisible: Boolean,
    val isPreviewPauseButtonVisible: Boolean,
    val previewTitle: String?,
    val previewProgressBarProgress: Int,
    val previewProgressBarMax: Int,
    val isPlayButtonVisible: Boolean,
    val isPauseButtonVisible: Boolean,
    val title: String?,
    val positionText: String,
    val progressBarProgress: Int,
    val progressBarMax: Int,
    val durationText: String,
    val coverUrl: String?
)