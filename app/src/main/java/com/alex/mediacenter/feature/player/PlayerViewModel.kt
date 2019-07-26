package com.alex.mediacenter.feature.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.bus.BottomSheetExpandEvent
import com.alex.mediacenter.bus.BottomSheetOffsetEvent
import com.alex.mediacenter.bus.MediaPlayerEvent
import com.alex.mediacenter.bus.RxBus
import com.alex.mediacenter.player.MediaPlayer
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    val previewAlphaState: LiveData<Float> = MutableLiveData()
    val playerExpandState: LiveData<Boolean> = MutableLiveData()
    val playerState: LiveData<PlayerState> = MutableLiveData()
    val messageState: LiveData<String> = MutableLiveData()

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            RxBus.listen(MediaPlayerEvent::class.java).subscribe {
                when (it.state) {
                    MediaPlayer.State.IDLE -> {
                        val state = PlayerState(
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

                        (playerState as MutableLiveData).postValue(state)
                    }
                    MediaPlayer.State.BUFFER -> {
                        val state = playerState.value?.copy(
                            previewProgressBarProgress = it.position.toInt(),
                            progressBarProgress = it.position.toInt(),
                            positionText = convertTimestampToString(it.position / 1000, it.duration / 100 >= 3600)
                        )

                        (playerState as MutableLiveData).postValue(state)
                    }
                    MediaPlayer.State.PLAY -> {
                        val state = PlayerState(
                            false,
                            true,
                            it.title,
                            it.position.toInt(),
                            it.duration.toInt(),
                            false,
                            true,
                            it.title,
                            convertTimestampToString(it.position / 1000, it.duration / 1000 >= 3600),
                            it.position.toInt(),
                            it.duration.toInt(),
                            convertTimestampToString(it.duration / 1000, it.duration / 1000 >= 3600),
                            it.imageUrl
                        )

                        (playerState as MutableLiveData).postValue(state)
                    }
                    MediaPlayer.State.PAUSE, MediaPlayer.State.END -> {
                        val state = playerState.value?.copy(
                            isPreviewPlayButtonVisible = true,
                            isPreviewPauseButtonVisible = false,
                            isPlayButtonVisible = true,
                            isPauseButtonVisible = false
                        )

                        (playerState as MutableLiveData).postValue(state)
                    }
                    MediaPlayer.State.ERROR -> (messageState as MutableLiveData).postValue("Could not load media")
                }
            }

            RxBus.listen(BottomSheetOffsetEvent::class.java).subscribe {
                if (it.offset != null) {
                    (previewAlphaState as MutableLiveData).postValue(1 - it.offset!!)
                }
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun clickOnPreview() {
        RxBus.publish(BottomSheetExpandEvent(true))
        (playerExpandState as MutableLiveData).postValue(true)
    }

    fun clickOnPlay() {
        if (MediaPlayer.currentState.state == MediaPlayer.State.END) {
            MediaPlayer.seek(0)
        }
        MediaPlayer.resume()
    }

    fun clickOnPause() {
        MediaPlayer.pause()
    }

    fun seekStart() {
    }

    fun seek(timestamp: Long) {
    }

    fun seekStop(timestamp: Long) {
        MediaPlayer.seek(timestamp)
    }

    // ----------------------------------------------------------------------------

    private fun convertTimestampToString(timestamp: Long, greaterThanOneHour: Boolean): String {
        return when (greaterThanOneHour) {
            true -> String.format("%02d:%02d:%02d", timestamp / 3600, (timestamp % 3600) / 60, (timestamp % 60))
            false -> String.format("%02d:%02d", (timestamp % 3600) / 60, (timestamp % 60))
        }
    }
}