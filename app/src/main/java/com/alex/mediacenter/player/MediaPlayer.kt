package com.alex.mediacenter.player

import android.content.Context
import android.net.Uri
import com.alex.mediacenter.bus.MediaPlayerEvent
import com.alex.mediacenter.bus.RxBus
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

object MediaPlayer {

    private lateinit var player: SimpleExoPlayer

    private var positionDisposable: Disposable? = null

    private lateinit var currentState: MediaPlayerEvent

    enum class State {
        IDLE,
        BUFFER,
        PLAY,
        PAUSE,
        END,
        ERROR
    }

    // ----------------------------------------------------------------------------

    fun init(context: Context) {
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())))
        player.addListener(object: Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException?) {
                currentState = MediaPlayerEvent(State.ERROR)
                RxBus.publish(currentState)

                disposePosition()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        currentState = MediaPlayerEvent(State.IDLE)
                        RxBus.publish(currentState)

                        disposePosition()
                    }
                    Player.STATE_BUFFERING -> {
                        currentState = currentState.copy(state = State.BUFFER)
                        RxBus.publish(currentState)

                        disposePosition()
                    }
                    Player.STATE_READY -> {
                        if (playWhenReady) {
                            currentState = currentState.copy(state = State.PLAY, duration = player.duration)
                            RxBus.publish(currentState)

                            observePosition()
                        } else {
                            currentState = currentState.copy(state = State.PAUSE)
                            RxBus.publish(currentState)

                            disposePosition()
                        }
                    }
                    Player.STATE_ENDED -> {
                        currentState = currentState.copy(state = State.END)
                        RxBus.publish(currentState)

                        disposePosition()
                    }
                }
            }
        })
    }

    fun play(streamUrl: String, title: String, imageUrl: String?) {
        val videoURI = Uri.parse(streamUrl)
        val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer")
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null)

        currentState = MediaPlayerEvent(MediaPlayer.State.IDLE, 0, 0, title, imageUrl)

        player.apply {
            prepare(mediaSource)
            player.playWhenReady = true
        }
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = true
    }

    fun seek(position: Long) {
        player.seekTo(position)
        player.next()
    }

    fun release() {
        player.stop()
    }

    // ----------------------------------------------------------------------------

    private fun observePosition() {
        positionDisposable = Observable
            .interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map { player.currentPosition to player.duration }
            .subscribe {
                currentState = currentState.copy(state = State.PLAY, position = it.first, duration = it.second)
                RxBus.publish(currentState)
            }
    }

    private fun disposePosition() {
        positionDisposable?.dispose()
    }
}