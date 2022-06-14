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

    var playerPreviewState: UiModelPlayerPreview by mutableStateOf(
        UiModelPlayerPreview(
            true,
            "",
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
                            null,
                            durationEmpty,
                            durationEmpty
                        )
                    }
                    MediaPlayer.Type.BUFFER -> {
                        playerPreviewState = playerPreviewState.copy(
                            showPlayButton = false,
                            progress = calculateProgress(state.position, state.duration),
                            position = convertTimestampToString(state.position)
                        )
                    }
                    MediaPlayer.Type.PLAY -> {
                        playerPreviewState = UiModelPlayerPreview(
                            false,
                            state.title ?: "",
                            calculateProgress(state.position, state.duration),
                            state.imageUrl,
                            convertTimestampToString(state.position),
                            convertTimestampToString(state.duration)
                        )
                    }
                    MediaPlayer.Type.PAUSE, MediaPlayer.Type.END -> {
                        playerPreviewState = playerPreviewState.copy(showPlayButton = true)
                    }
                    MediaPlayer.Type.ERROR -> {
                        playerPreviewState = UiModelPlayerPreview(
                            true,
                            "",
                            0f,
                            null,
                            durationEmpty,
                            durationEmpty)
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

    fun seekStop(timestamp: Long) {
        mediaPlayer.seek(timestamp)
    }

    // ----------------------------------------------------------------------------

    private fun convertTimestampToString(timestamp: Long): String {
        return with(timestamp / 1000) {
            String.format(durationFormat, this / 3600, (this % 3600) / 60, (this % 60))
        }
    }

    private fun calculateProgress(position: Long, duration: Long): Float {
        return when (position > 0 && duration > 0) {
            true -> position.toFloat() / duration.toFloat()
            false -> 0.0f
        }
    }
}