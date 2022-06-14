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
        val position: Long = 0,
        val duration: Long = 0,
        val title: String? = null,
        val imageUrl: String? = null
    )

    enum class Type {
        IDLE,
        BUFFER,
        PLAY,
        PAUSE,
        END,
        ERROR
    }

    // ----------------------------------------------------------------------------

    init {
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                _currentState.value = _currentState.value.copy(type = Type.ERROR)

                disposePosition()

                println(_currentState.value.toString())
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                _currentState.value = when (playbackState) {
                    Player.STATE_IDLE -> _currentState.value.copy(type = Type.IDLE)
                    Player.STATE_BUFFERING -> _currentState.value.copy(
                        type = Type.BUFFER,
                        position = player.currentPosition,
                        duration = player.duration
                    )
                    Player.STATE_READY -> {
                        when (playWhenReady) {
                            true -> _currentState.value.copy(type = Type.PLAY, duration = player.duration)
                            false -> _currentState.value.copy(type = Type.PAUSE)
                        }
                    }
                    Player.STATE_ENDED -> _currentState.value.copy(type = Type.END)
                    else -> _currentState.value.copy(type = Type.ERROR)
                }

                if (playbackState == Player.STATE_READY && playWhenReady) observePosition() else disposePosition()

                println(currentState.toString())
            }
        })
    }

    // ----------------------------------------------------------------------------

    fun play(streamUrl: String, title: String, imageUrl: String?) {
        _currentState.value = State(Type.IDLE, 0, 0, title, imageUrl)

        player.apply {
            setMediaItem(MediaItem.fromUri(streamUrl))
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

    fun release() {
        player.stop()
    }

    // ----------------------------------------------------------------------------

    private fun observePosition() {
        jobPosition = GlobalScope.launch(Dispatchers.Main) {
            repeat(player.duration.toInt()) {
                _currentState.value = _currentState.value.copy(position = player.currentPosition)

                delay(1_000)

                println(currentState.toString())
            }
        }
    }

    private fun disposePosition() {
        jobPosition?.cancel()
        jobPosition = null
    }
}