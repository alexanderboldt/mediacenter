package com.alex.mediacenter.feature.dummy

import androidx.lifecycle.ViewModel
import com.alex.mediacenter.player.MediaPlayer

class DummyViewModel(private val mediaPlayer: MediaPlayer) : ViewModel() {

    fun clickOnReleasePlayer() {
        mediaPlayer.release()
    }

    fun clickOnButtonOne() {
        mediaPlayer.play(
            "http://mp3.podcast.hr-online.de/mp3/podcast/lateline/lateline_20190611_81784966.mp3",
            "Lateline",
            "https://www.fritz.de/content/dam/rbb/frz/standard/lateline.jpg.jpg/rendition=fritzlateline1280.jpg/size=470x264.jpg")
    }

    fun clickOnButtonTwo() {
        mediaPlayer.play(
            "https://rbbmediapmdp-a.akamaihd.net/content/ea/f7/eaf72489-96e0-4e92-b230-3d3e31924458/108783cb-172e-4dc2-8ef4-2c44e60e7945_7056089a-d58f-4766-9f31-a05310aed0e5.mp3",
            "Blue Moon",
            "https://www.fritz.de/content/dam/rbb/frz/podcasts/blue_moon.jpg.png/size=470x264.png")
    }
}