package com.alex.mediacenter.feature.dummy

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.player.MediaPlayer
import com.alexstyl.warden.Warden
import kotlinx.coroutines.launch

class DummyViewModel(
    private val mediaPlayer: MediaPlayer,
    private val warden: Warden
) : ViewModel() {

    init {
        viewModelScope.launch {
            warden.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // ----------------------------------------------------------------------------

    fun onClickReleasePlayer() {
        mediaPlayer.release()
    }

    fun onClickButtonOne() {
        mediaPlayer.play(
            listOf(
                "/sdcard/Samsung/Music/Over_the_Horizon.mp3",
                "http://mp3.podcast.hr-online.de/mp3/podcast/lateline/lateline_20190611_81784966.mp3"
            )
        )
    }

    fun onClickButtonTwo() {
        mediaPlayer.play(
            listOf("https://rbbmediapmdp-a.akamaihd.net/content/ea/f7/eaf72489-96e0-4e92-b230-3d3e31924458/108783cb-172e-4dc2-8ef4-2c44e60e7945_7056089a-d58f-4766-9f31-a05310aed0e5.mp3")
        )
    }
}