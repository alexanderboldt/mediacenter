package com.alex.mediacenter.player

import android.content.Context
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.MediaItem as ExoPlayerMediaItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPlayer(context: Context) {

    private var player = ExoPlayer.Builder(context).build()

    private var jobPosition: Job? = null

    private val _currentState = MutableStateFlow(MediaState())
    val currentState = _currentState.asStateFlow()

    // ----------------------------------------------------------------------------

    init {
        player.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                val mediaItems = (0 until player.mediaItemCount).toList().map {
                    val mediaItem = player.getMediaItemAt(it)
                    MediaItem(mediaItem.mediaMetadata.title.toString(), mediaItem.localConfiguration!!.uri.toString())
                }

                _currentState.value = when {
                    // idle
                    player.playbackState == Player.STATE_IDLE -> MediaState()
                    // buffering
                    player.playbackState == Player.STATE_BUFFERING -> _currentState.value.copy(
                        mediaType = MediaType.BUFFER,
                        position = player.currentPosition,
                        duration = player.duration,
                        currentMediaItemIndex = player.currentMediaItemIndex,
                        mediaItems = mediaItems
                    )
                    // playing
                    player.playbackState == Player.STATE_READY && player.playWhenReady -> {
                        disposePosition()
                        observePosition()
                        _currentState.value.copy(
                            mediaType = MediaType.PLAY,
                            duration = player.duration,
                            currentMediaItemIndex = player.currentMediaItemIndex,
                            mediaItems = mediaItems
                        )
                    }
                    // pausing
                    player.playbackState == Player.STATE_READY && !player.playWhenReady -> {
                        disposePosition()
                        _currentState.value.copy(mediaType = MediaType.PAUSE, mediaItems = mediaItems)
                    }
                    // ended
                    else -> _currentState.value.copy(mediaType = MediaType.END, mediaItems = mediaItems)
                }
            }
        })
    }

    // ----------------------------------------------------------------------------

    fun play(urls: List<String>, startIndex: Int) {
        player.apply {
            setMediaItems(urls.map { it.toExoPlayerMediaItem() }, startIndex, 0)
            playWhenReady = true
            prepare()
        }
    }

    fun add(url: String) {
        player.addMediaItem(player.currentMediaItemIndex.inc(), url.toExoPlayerMediaItem())
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = true
    }

    fun seek(position: Long) {
        player.seekTo(position)
    }

    fun replay(time: Long) {
        player.seekTo(_currentState.value.position - time)
    }

    fun forward(time: Long) {
        player.seekTo(_currentState.value.position + time)
    }

    fun previous() {
        player.seekToPreviousMediaItem()
    }

    fun next() {
        player.seekToNextMediaItem()
    }

    fun release() {
        player.stop()
    }

    // ----------------------------------------------------------------------------

    private fun observePosition() {
        jobPosition = GlobalScope.launch(Dispatchers.Main) {
            repeat(player.duration.toInt()) {
                _currentState.value = _currentState.value.copy(position = player.currentPosition)

                delay(1_000)
            }
        }
    }

    private fun disposePosition() {
        jobPosition?.cancel()
        jobPosition = null
    }

    private fun String.toExoPlayerMediaItem(): ExoPlayerMediaItem {
        return ExoPlayerMediaItem
            .Builder()
            .setUri(this)
            .setMediaMetadata(
                MediaMetadata
                    .Builder()
                    .setTitle(this.split("/").last().split(".").first())
                    .build()
            ).build()
    }
}