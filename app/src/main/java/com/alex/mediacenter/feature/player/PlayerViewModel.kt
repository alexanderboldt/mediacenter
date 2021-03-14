package com.alex.mediacenter.feature.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.feature.player.model.UiModelPlayerState
import com.alex.mediacenter.player.MediaPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(private val mediaPlayer: MediaPlayer) : ViewModel() {

    private val _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int> = _bottomSheetState

    private val _previewAlphaState = MutableLiveData<Float>()
    val previewAlphaState: LiveData<Float> = _previewAlphaState

    private val _playerState = MutableLiveData<UiModelPlayerState>()
    val playerState: LiveData<UiModelPlayerState> = _playerState

    private val _messageState = MutableLiveData<String>()
    val messageState: LiveData<String> = _messageState

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            mediaPlayer.currentState.collect { state ->
                when (state.type) {
                    MediaPlayer.Type.IDLE -> {
                        UiModelPlayerState(
                            true,
                            false,
                            null,
                            0,
                            0,
                            true,
                            false,
                            null,
                            "00:00:00",
                            0,
                            0,
                            "00:00:00",
                            null
                        )
                    }
                    MediaPlayer.Type.BUFFER -> {
                        playerState.value?.copy(
                            previewProgressBarProgress = state.position.toInt(),
                            progressBarProgress = state.position.toInt(),
                            positionText = convertTimestampToString(
                                state.position / 1000,
                                state.duration / 100 >= 3600
                            )
                        )
                    }
                    MediaPlayer.Type.PLAY -> {
                        UiModelPlayerState(
                            false,
                            true,
                            state.title,
                            state.position.toInt(),
                            state.duration.toInt(),
                            false,
                            true,
                            state.title,
                            convertTimestampToString(
                                state.position / 1000,
                                state.duration / 1000 >= 3600
                            ),
                            state.position.toInt(),
                            state.duration.toInt(),
                            convertTimestampToString(
                                state.duration / 1000,
                                state.duration / 1000 >= 3600
                            ),
                            state.imageUrl
                        )
                    }
                    MediaPlayer.Type.PAUSE, MediaPlayer.Type.END -> {
                        playerState.value?.copy(
                            isPreviewPlayButtonVisible = true,
                            isPreviewPauseButtonVisible = false,
                            isPlayButtonVisible = true,
                            isPauseButtonVisible = false
                        )
                    }
                    MediaPlayer.Type.ERROR -> {
                        _messageState.postValue("Could not load media")

                        UiModelPlayerState(
                            true,
                            false,
                            null,
                            0,
                            0,
                            true,
                            false,
                            null,
                            "00:00:00",
                            0,
                            0,
                            "00:00:00",
                            null
                        )
                    }
                }.also {
                    _playerState.postValue(it) }
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun onBottomSheetOffset(offset: Float) {
        _previewAlphaState.postValue(1 - offset)
    }

    fun onClickPreview() {
        _bottomSheetState.postValue(BottomSheetBehavior.STATE_EXPANDED)
    }

    fun onClickPlay() {
        if (mediaPlayer.currentState.value.type == MediaPlayer.Type.END) {
            mediaPlayer.seek(0)
        }
        mediaPlayer.resume()
    }

    fun clickOnPause() {
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
}