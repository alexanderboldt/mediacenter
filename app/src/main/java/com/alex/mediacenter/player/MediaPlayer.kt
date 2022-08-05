package com.alex.mediacenter.player

import android.content.Context
import com.google.android.exoplayer2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPlayer(context: Context) {

    private var player = ExoPlayer.Builder(context).build()

    private var jobPosition: Job? = null

    private val _currentState = MutableStateFlow(State())
    val currentState = _currentState.asStateFlow()

    // ----------------------------------------------------------------------------

    data class State(
        val type: Type = Type.IDLE,
        val position: Long = 0, // in milliseconds
        val duration: Long = 0, // in milliseconds
        val title: String = ""
    )

    enum class Type {
        IDLE,
        BUFFER,
        PLAY,
        PAUSE,
        END
    }

    // ----------------------------------------------------------------------------

    init {
        player.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                _currentState.value = when {
                    // idle
                    player.playbackState == Player.STATE_IDLE -> State()
                    // buffering
                    player.playbackState == Player.STATE_BUFFERING -> _currentState.value.copy(
                        type = Type.BUFFER,
                        position = player.currentPosition,
                        duration = player.duration,
                        title = player.mediaMetadata.title.toString()
                    )
                    // playing
                    player.playbackState == Player.STATE_READY && player.playWhenReady -> {
                        disposePosition()
                        observePosition()
                        _currentState.value.copy(
                            type = Type.PLAY,
                            duration = player.duration,
                            title = player.mediaMetadata.title.toString()
                        )
                    }
                    // pausing
                    player.playbackState == Player.STATE_READY && !player.playWhenReady -> {
                        disposePosition()
                        _currentState.value.copy(type = Type.PAUSE)
                    }
                    // ended
                    else -> _currentState.value.copy(type = Type.END)
                }
            }
        })
    }

    // ----------------------------------------------------------------------------

    fun play(urls: List<String>, startIndex: Int) {
        player.apply {
            setMediaItems(urls.map { MediaItem.fromUri(it) }, startIndex, 0)
            playWhenReady = true
            prepare()
        }
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
}