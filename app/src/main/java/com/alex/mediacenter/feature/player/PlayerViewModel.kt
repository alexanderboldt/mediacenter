package com.alex.mediacenter.feature.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.feature.player.model.UiModelPlayerPreview
import com.alex.mediacenter.player.MediaPlayer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(private val mediaPlayer: MediaPlayer) : ViewModel() {

    var playerPreviewState: UiModelPlayerPreview by mutableStateOf(
        UiModelPlayerPreview(
            true,
            "",
            0f,
            null,
            "00:00:00",
            "00:00:00"
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
                            "00:00:00",
                            "00:00:00"
                        )
                    }
                    MediaPlayer.Type.BUFFER -> {
                        playerPreviewState = playerPreviewState.copy(
                            showPlayButton = false,
                            progress = calculateProgress(state.position, state.duration),
                            position = convertTimestampToString(
                                state.position / 1000,
                                state.duration / 1000 >= 3600
                            ))
                    }
                    MediaPlayer.Type.PLAY -> {
                        playerPreviewState = UiModelPlayerPreview(
                            false,
                            state.title ?: "",
                            calculateProgress(state.position, state.duration),
                            state.imageUrl,
                            convertTimestampToString(
                                state.position / 1000,
                                state.duration / 1000 >= 3600
                            ),
                            convertTimestampToString(
                                state.duration / 1000,
                                state.duration / 1000 >= 3600
                            ))
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
                            "00:00:00",
                            "00:00:00")
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

    private fun convertTimestampToString(timestamp: Long, greaterThanOneHour: Boolean): String {
        return when (greaterThanOneHour) {
            true -> String.format("%02d:%02d:%02d", timestamp / 3600, (timestamp % 3600) / 60, (timestamp % 60))
            false -> String.format("%02d:%02d", (timestamp % 3600) / 60, (timestamp % 60))
        }
    }

    private fun calculateProgress(position: Long, duration: Long): Float {
        return when (position > 0 && duration > 0) {
            true -> position.toFloat() / duration.toFloat()
            false -> 0.0f
        }
    }
}