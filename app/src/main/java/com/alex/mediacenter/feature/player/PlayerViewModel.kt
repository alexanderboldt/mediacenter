package com.alex.mediacenter.feature.player

import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.R
import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.feature.base.ResourceProvider
import com.alex.mediacenter.feature.player.model.State
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.player.MediaType
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
            0f,
            0f,
            durationEmpty,
            durationEmpty,
            0,
            null
        )
    )

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            mediaPlayer.currentState.collect { player ->
                when (player.mediaType) {
                    MediaType.IDLE -> {
                        state.playerPreview = State.PlayerPreview(
                            true,
                            0f,
                            0f,
                            durationEmpty,
                            durationEmpty,
                            0,
                            null
                        )
                    }
                    MediaType.BUFFER -> {
                        state.playerPreview = state.playerPreview.copy(
                            showPlayButton = false,
                            progress = player.position.toFloat(),
                            positionFormatted = player.position.format(),
                            currentMediaItemIndex = player.currentMediaItemIndex,
                            mediaItems = player.mediaItems?.map { State.MediaItem(it.title) }
                        )
                    }
                    MediaType.PLAY -> {
                        state.playerPreview = State.PlayerPreview(
                            false,
                            player.position.toFloat(),
                            player.duration.toFloat(),
                            player.position.format(),
                            player.duration.format(),
                            currentMediaItemIndex = player.currentMediaItemIndex,
                            mediaItems = player.mediaItems?.map { State.MediaItem(it.title) }
                        )
                    }
                    MediaType.PAUSE, MediaType.END -> {
                        state.playerPreview = state.playerPreview.copy(
                            showPlayButton = true,
                            mediaItems = player.mediaItems?.map { State.MediaItem(it.title) }
                        )
                    }
                }
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun onClickPlay() {
        if (mediaPlayer.currentState.value.mediaType == MediaType.END) {
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