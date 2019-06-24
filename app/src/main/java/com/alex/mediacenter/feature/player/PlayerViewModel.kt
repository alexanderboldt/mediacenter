package com.alex.mediacenter.feature.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.mediacenter.bus.BottomSheetEvent
import com.alex.mediacenter.bus.MediaPlayerEvent
import com.alex.mediacenter.bus.RxBus
import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.util.plusAssign

class PlayerViewModel : BaseViewModel() {

    val previewAlphaState: LiveData<Float> = MutableLiveData()
    val playerState: LiveData<PlayerState> = MutableLiveData()
    val messageState: LiveData<String> = MutableLiveData()

    // ----------------------------------------------------------------------------

    init {
        disposables += RxBus.listen(MediaPlayerEvent::class.java).subscribe {
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
                    val state = PlayerState(
                        false,
                        true,
                        it.title,
                        0,
                        0,
                        false,
                        true,
                        it.title,
                        "00:00:00",
                        0,
                        0,
                        "00:00:00",
                        it.imageUrl
                    )

                    (playerState as MutableLiveData).postValue(state)
                }
                MediaPlayer.State.PLAY -> {

                    // convert from milliseconds to seconds
                    val position = it.position / 1000
                    val duration = it.duration / 1000

                    // check if duration is greater than one hour
                    val positionText = when (duration > 3600) {
                        true -> String.format("%02d:%02d:%02d", position / 3600, (position % 3600) / 60, (position % 60))
                        false -> String.format("%02d:%02d", (position % 3600) / 60, (position % 60))
                    }

                    val durationText = when (duration > 3600) {
                        true -> String.format("%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60))
                        false -> String.format("%02d:%02d", (duration % 3600) / 60, (duration % 60))
                    }

                    val state = PlayerState(
                        false,
                        true,
                        it.title,
                        it.position.toInt(),
                        it.duration.toInt(),
                        false,
                        true,
                        it.title,
                        positionText,
                        it.position.toInt(),
                        it.duration.toInt(),
                        durationText,
                        it.imageUrl
                    )

                    (playerState as MutableLiveData).postValue(state)
                }
                MediaPlayer.State.PAUSE, MediaPlayer.State.END -> {
                    val state = playerState.value?.copy(
                        isPreviewPlayButtonVisible =true,
                        isPreviewPauseButtonVisible = false,
                        isPlayButtonVisible = true,
                        isPauseButtonVisible = false)

                    (playerState as MutableLiveData).postValue(state)
                }
                MediaPlayer.State.ERROR -> (messageState as MutableLiveData).postValue("Could not load media")
            }
        }

        disposables += RxBus.listen(BottomSheetEvent::class.java).subscribe {
            if (it.offset != null) {
                (previewAlphaState as MutableLiveData).postValue(1 - it.offset!!)
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun clickOnPlay() {
        if (MediaPlayer.currentState.state == MediaPlayer.State.END) {
            MediaPlayer.seek(0)
        }
        MediaPlayer.resume()
    }

    fun clickOnPause() {
        MediaPlayer.pause()
    }
}