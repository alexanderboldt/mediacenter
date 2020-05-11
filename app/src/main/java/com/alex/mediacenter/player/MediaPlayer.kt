package com.alex.mediacenter.player

import android.content.Context
import android.net.Uri
import com.alex.core.bus.RxBus
import com.alex.mediacenter.bus.MediaPlayerEvent
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

object MediaPlayer {

    private lateinit var player: SimpleExoPlayer

    private var positionDisposable: Disposable? = null

    // ----------------------------------------------------------------------------

    var currentState = MediaPlayerEvent(State.IDLE)

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
        player = SimpleExoPlayer.Builder(context).build()
        player.addListener(object: Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                currentState = MediaPlayerEvent(State.ERROR)
                RxBus.publish(currentState)

                disposePosition()

                Timber.d(State.ERROR.name)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                currentState = when (playbackState) {
                    Player.STATE_IDLE -> MediaPlayerEvent(State.IDLE)
                    Player.STATE_BUFFERING -> currentState.copy(position = player.currentPosition, duration = player.duration, state = State.BUFFER)
                    Player.STATE_READY -> {
                        when (playWhenReady) {
                            true -> currentState.copy(state = State.PLAY, duration = player.duration)
                            false -> currentState.copy(state = State.PAUSE)
                        }
                    }
                    Player.STATE_ENDED -> currentState.copy(state = State.END)
                    else -> MediaPlayerEvent(State.ERROR)
                }

                RxBus.publish(currentState)

                if (playbackState == Player.STATE_READY && playWhenReady) observePosition() else disposePosition()

                Timber.d(currentState.toString())
            }
        })
    }

    fun play(streamUrl: String, title: String, imageUrl: String?) {
        val videoURI = Uri.parse(streamUrl)
        val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer")
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory).createMediaSource(videoURI)

        currentState = MediaPlayerEvent(State.IDLE, 0, 0, title, imageUrl)

        player.apply {
            prepare(mediaSource)
            playWhenReady = true
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
    }

    fun release() {
        player.stop()
    }

    // ----------------------------------------------------------------------------

    private fun observePosition() {
        positionDisposable = Observable
            .interval(0,1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map { player.currentPosition to player.duration }
            .subscribe {
                currentState = currentState.copy(state = State.PLAY, position = it.first, duration = it.second)

                RxBus.publish(currentState)

                Timber.d(currentState.toString())
            }
    }

    private fun disposePosition() {
        positionDisposable?.dispose()
    }
}