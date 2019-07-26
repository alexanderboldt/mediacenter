package com.alex.mediacenter.feature.player

import android.view.View
import android.widget.Toast
import com.alex.mediacenter.R
import com.alex.mediacenter.databinding.ControllerPlayerBinding
import com.alex.mediacenter.feature.base.BaseController
import com.alex.mediacenter.util.observe
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.*
import io.reactivex.Observable

class PlayerController : BaseController<ControllerPlayerBinding>(R.layout.controller_player) {

    private val viewModel by lazy { viewModelProvider().get(PlayerViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onSetupViewBinding() {
        disposables += binding.constraintLayoutPreview.clicks().subscribe {
            viewModel.clickOnPreview()
        }

        disposables += Observable
            .merge(binding.imageViewPreviewPlay.clicks(), binding.imageViewPlay.clicks())
            .subscribe {
                viewModel.clickOnPlay()
            }

        disposables += Observable
            .merge(binding.imageViewPreviewPause.clicks(), binding.imageViewPause.clicks())
            .subscribe {
                viewModel.clickOnPause()
            }

        disposables += binding.seekBar.changeEvents().subscribe {
            when (it) {
                is SeekBarStartChangeEvent -> viewModel.seekStart()
                is SeekBarStopChangeEvent -> viewModel.seekStop(it.view().progress.toLong())
                is SeekBarProgressChangeEvent -> if (it.fromUser()) viewModel.seek(it.progress().toLong())
            }
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.previewAlphaState.observe(this) {
            binding.constraintLayoutPreview.alpha = it
        }

        viewModel.playerState.observe(this) { state ->
            binding.imageViewPreviewPlay.visibility = if (state.isPreviewPlayButtonVisible) View.VISIBLE else View.INVISIBLE
            binding.imageViewPreviewPause.visibility = if (state.isPreviewPauseButtonVisible) View.VISIBLE else View.INVISIBLE
            binding.textViewPreviewTitle.text = state.previewTitle
            binding.progressBarPreview.progress = state.previewProgressBarProgress
            binding.progressBarPreview.max = state.previewProgressBarMax

            binding.imageViewPlay.visibility = if (state.isPlayButtonVisible) View.VISIBLE else View.INVISIBLE
            binding.imageViewPause.visibility = if (state.isPauseButtonVisible) View.VISIBLE else View.INVISIBLE
            binding.textViewTitle.text = state.title
            binding.textViewPosition.text = state.positionText
            binding.seekBar.progress = state.progressBarProgress
            binding.seekBar.max = state.progressBarMax
            binding.textViewDuration.text = state.durationText

            if (state.coverUrl == null) {
                binding.imageViewPreviewCover.setImageDrawable(null)
                binding.imageViewBackground.setImageDrawable(null)
                binding.imageViewCover.setImageDrawable(null)
            } else {
                binding.imageViewPreviewCover.setImage(state.coverUrl)
                binding.imageViewBackground.setImage(state.coverUrl, true)
                binding.imageViewCover.setImage(state.coverUrl)
            }
        }

        viewModel.messageState.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}