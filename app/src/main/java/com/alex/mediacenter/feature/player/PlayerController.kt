package com.alex.mediacenter.feature.player

import android.view.View
import android.widget.Toast
import com.alex.mediacenter.R
import com.alex.mediacenter.bus.BottomSheetEvent
import com.alex.mediacenter.bus.MediaPlayerEvent
import com.alex.mediacenter.bus.RxBus
import com.alex.mediacenter.databinding.ControllerPlayerBinding
import com.alex.mediacenter.feature.base.BaseController
import com.alex.mediacenter.player.MediaPlayer
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable

class PlayerController : BaseController<ControllerPlayerBinding>(R.layout.controller_player) {

    override fun onSetupView() {}

    override fun onSetupViewBinding() {
        disposables += RxBus.listen(MediaPlayerEvent::class.java).subscribe {
            when (it.state) {
                MediaPlayer.State.IDLE -> {
                    binding.imageViewPreviewPlay.visibility = View.VISIBLE
                    binding.imageViewPreviewPause.visibility = View.INVISIBLE

                    binding.imageViewPlay.visibility = View.VISIBLE
                    binding.imageViewPause.visibility = View.INVISIBLE

                    binding.textViewPreviewTitle.text = null
                    binding.textViewTitle.text = null

                    binding.progressBarPreview.progress = 0
                    binding.progressBar.progress = 0

                    binding.textViewPosition.text = "00:00:00"
                    binding.textViewDuration.text = "00:00:00"

                    binding.imageViewBackground.setImageDrawable(null)
                    binding.imageViewCover.setImageDrawable(null)
                }
                MediaPlayer.State.BUFFER -> {
                    binding.imageViewPreviewPlay.visibility = View.INVISIBLE
                    binding.imageViewPreviewPause.visibility = View.VISIBLE

                    binding.imageViewPlay.visibility = View.INVISIBLE
                    binding.imageViewPause.visibility = View.VISIBLE

                    binding.textViewPreviewTitle.text = it.title
                    binding.textViewTitle.text = it.title

                    binding.progressBarPreview.progress = 0
                    binding.progressBar.progress = 0

                    binding.imageViewBackground.setImage(it.imageUrl, true)
                    binding.imageViewCover.setImage(it.imageUrl)

                    binding.textViewPosition.text = "00:00:00"
                    binding.textViewDuration.text = "00:00:00"
                }
                MediaPlayer.State.PLAY -> {
                    binding.imageViewPreviewPlay.visibility = View.INVISIBLE
                    binding.imageViewPreviewPause.visibility = View.VISIBLE

                    binding.imageViewPlay.visibility = View.INVISIBLE
                    binding.imageViewPause.visibility = View.VISIBLE

                    binding.textViewPreviewTitle.text = it.title
                    binding.textViewTitle.text = it.title

                    binding.imageViewBackground.setImage(it.imageUrl, true)
                    binding.imageViewCover.setImage(it.imageUrl)

                    binding.progressBarPreview.max = it.duration.toInt()
                    binding.progressBarPreview.progress = it.position.toInt()

                    // convert from milliseconds to seconds
                    val position = it.position / 1000
                    val duration = it.duration / 1000

                    // check if duration is greater than one hour
                    binding.textViewPosition.text = when (duration > 3600) {
                        true -> String.format("%02d:%02d:%02d", position / 3600, (position % 3600) / 60, (position % 60))
                        false -> String.format("%02d:%02d", (position % 3600) / 60, (position % 60))
                    }

                    binding.textViewDuration.text = when (duration > 3600) {
                        true -> String.format("%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60))
                        false -> String.format("%02d:%02d", (duration % 3600) / 60, (duration % 60))
                    }

                    binding.progressBar.max = it.duration.toInt()
                    binding.progressBar.progress = it.position.toInt()
                }
                MediaPlayer.State.PAUSE -> {
                    binding.imageViewPreviewPlay.visibility = View.VISIBLE
                    binding.imageViewPreviewPause.visibility = View.INVISIBLE

                    binding.imageViewPlay.visibility = View.VISIBLE
                    binding.imageViewPause.visibility = View.INVISIBLE
                }
                MediaPlayer.State.END -> {
                    binding.imageViewPreviewPlay.visibility = View.VISIBLE
                    binding.imageViewPreviewPause.visibility = View.INVISIBLE

                    binding.imageViewPlay.visibility = View.VISIBLE
                    binding.imageViewPause.visibility = View.INVISIBLE
                }
                MediaPlayer.State.ERROR -> Toast.makeText(context, "Could not load media", Toast.LENGTH_LONG).show()
            }
        }

        disposables += RxBus.listen(BottomSheetEvent::class.java).subscribe {
            if (it.offset != null) {
                binding.constraintLayoutPreview.alpha = 1 - it.offset!!
            }
        }

        disposables += Observable
            .merge(binding.imageViewPreviewPlay.clicks(), binding.imageViewPlay.clicks())
            .subscribe {
                MediaPlayer.resume()
            }

        disposables += Observable
            .merge(binding.imageViewPreviewPause.clicks(), binding.imageViewPause.clicks())
            .subscribe {
                MediaPlayer.pause()
            }
    }

    override fun onSetupViewModelBinding() {}
}