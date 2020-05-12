package com.alex.mediacenter.feature.dummy

import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.player.MediaPlayer

class DummyViewModel : BaseViewModel() {

    fun clickOnReleasePlayer() {
        MediaPlayer.release()
    }

    fun clickOnButtonOne() {
        MediaPlayer.play(
            "http://mp3.podcast.hr-online.de/mp3/podcast/lateline/lateline_20190611_81784966.mp3",
            "Lateline",
            "https://www.fritz.de/content/dam/rbb/frz/standard/lateline.jpg.jpg/rendition=fritzlateline1280.jpg/size=470x264.jpg")
    }

    fun clickOnButtonTwo() {
        MediaPlayer.play(
            "https://rbbmediapmdp-a.akamaihd.net/content/fb/88/4003d4ce-3e24-405c-b01f-fcae15996f90/d3330534-55c1-4c6c-b8b2-457faf023c96_a4ad27c5-2f83-4161-8455-b6bff9af72a4.mp3",
            "Blue Moon",
            "https://www.fritz.de/content/dam/rbb/frz/podcasts/blue_moon.jpg.png/size=470x264.png")
    }
}