package com.alex.mediacenter.feature.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.R
import com.alex.mediacenter.feature.base.ResourceProvider
import com.alex.mediacenter.feature.player.model.UiModelPlayerPreview
import com.alex.mediacenter.player.MediaPlayer
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayer: MediaPlayer,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val durationEmpty by lazy { resourceProvider.getString(R.string.player_duration_empty) }
    private val durationFormat by lazy { resourceProvider.getString(R.string.player_duration_format) }

    private val previousOrReplayThreshold = 5_000 // in milliseconds

    var playerPreviewState: UiModelPlayerPreview by mutableStateOf(
        UiModelPlayerPreview(
            true,
            "",
            0f,
            0f,
            null,
            durationEmpty,
            durationEmpty
        ))
        private set

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            mediaPlayer.currentState.collect { state ->
                when (state.type) {
                    MediaPlayer.Type.IDLE -> {
                        playerPreviewState = UiModelPlayerPreview(
                            true,
                            "",
                            0f,
                            0f,
                            null,
                            durationEmpty,
                            durationEmpty
                        )
                    }
                    MediaPlayer.Type.BUFFER -> {
                        playerPreviewState = playerPreviewState.copy(
                            showPlayButton = false,
                            progress = state.position.toFloat(),
                            positionFormatted = state.position.format()
                        )
                    }
                    MediaPlayer.Type.PLAY -> {
                        playerPreviewState = UiModelPlayerPreview(
                            false,
                            state.title ?: "",
                            state.position.toFloat(),
                            state.duration.toFloat(),
                            null,
                            state.position.format(),
                            state.duration.format()
                        )
                    }
                    MediaPlayer.Type.PAUSE, MediaPlayer.Type.END -> {
                        playerPreviewState = playerPreviewState.copy(showPlayButton = true)
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

    // ----------------------------------------------------------------------------

    private fun Long.format() = with(this / 1000) {
        String.format(durationFormat, this / 3600, (this % 3600) / 60, (this % 60))
    }
}