package com.alex.mediacenter.feature.player

import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.R
import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.feature.base.ResourceProvider
import com.alex.mediacenter.feature.player.model.State
import com.alex.mediacenter.player.MediaPlayer
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayer: MediaPlayer,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<State, Unit>() {

    private val durationEmpty by lazy { resourceProvider.getString(R.string.player_duration_empty) }
    private val durationFormat by lazy { resourceProvider.getString(R.string.player_duration_format) }

    private val previousOrReplayThreshold = 5_000 // in milliseconds
    private val seekBy = 10_000L // in milliseconds

    override val state = State(
        State.PlayerPreview(
            true,
            "",
            0f,
            0f,
            durationEmpty,
            durationEmpty
        )
    )

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            mediaPlayer.currentState.collect { player ->
                when (player.type) {
                    MediaPlayer.Type.IDLE -> {
                        state.playerPreview = State.PlayerPreview(
                            true,
                            "",
                            0f,
                            0f,
                            durationEmpty,
                            durationEmpty
                        )
                    }
                    MediaPlayer.Type.BUFFER -> {
                        state.playerPreview = state.playerPreview.copy(
                            showPlayButton = false,
                            progress = player.position.toFloat(),
                            positionFormatted = player.position.format()
                        )
                    }
                    MediaPlayer.Type.PLAY -> {
                        state.playerPreview = State.PlayerPreview(
                            false,
                            player.title ?: "",
                            player.position.toFloat(),
                            player.duration.toFloat(),
                            player.position.format(),
                            player.duration.format()
                        )
                    }
                    MediaPlayer.Type.PAUSE, MediaPlayer.Type.END -> {
                        state.playerPreview = state.playerPreview.copy(showPlayButton = true)
                    }
                }
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun onClickPlay() {
        if (mediaPlayer.currentState.value.type == MediaPlayer.Type.END) {
            mediaPlayer.seek(0)
        }
        mediaPlayer.resume()
    }

    fun onClickPause() {
        mediaPlayer.pause()
    }

    fun onSeek(timestamp: Float) {
        mediaPlayer.seek(timestamp.toLong())
    }

    fun onClickPrevious() {
        // either go to the previous song or start the current song from the beginning
        when (mediaPlayer.currentState.value.position >= previousOrReplayThreshold) {
            true -> mediaPlayer.seek(0)
            false -> mediaPlayer.previous()
        }

        mediaPlayer.resume()
    }

    fun onClickNext() {
        mediaPlayer.next()
        mediaPlayer.resume()
    }

    fun onClickReplay() {
        mediaPlayer.replay(seekBy)
    }

    fun onClickForward() {
        mediaPlayer.forward(seekBy)
    }

    // ----------------------------------------------------------------------------

    private fun Long.format() = with(this / 1000) {
        String.format(durationFormat, this / 3600, (this % 3600) / 60, (this % 60))
    }
}