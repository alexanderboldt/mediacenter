package com.alex.mediacenter.player

data class MediaState(
    val mediaType: MediaType = MediaType.IDLE,
    val position: Long = 0, // in milliseconds
    val duration: Long = 0, // in milliseconds
    val currentMediaItemIndex: Int = 0,
    val mediaItems: List<MediaItem>? = null
)

enum class MediaType {
    IDLE,
    BUFFER,
    PLAY,
    PAUSE,
    END
}

data class MediaItem(
    val title: String,
    val url: String
)