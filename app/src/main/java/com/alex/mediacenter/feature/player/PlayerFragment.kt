package com.alex.mediacenter.feature.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.alex.mediacenter.databinding.ControllerPlayerBinding
import com.alex.mediacenter.feature.base.BaseFragment
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.SeekBarProgressChangeEvent
import com.jakewharton.rxbinding4.widget.SeekBarStartChangeEvent
import com.jakewharton.rxbinding4.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding4.widget.changeEvents
import io.reactivex.rxjava3.core.Observable

class PlayerFragment : BaseFragment<ControllerPlayerBinding>() {

    private val viewModel: PlayerViewModel by viewModels()

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): ControllerPlayerBinding {
        return ControllerPlayerBinding.inflate(inflater, container, false)
    }

    override fun bindView() {
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

        disposables += binding.seekBar.changeEvents().subscribe { event ->
            when (event) {
                is SeekBarStartChangeEvent -> viewModel.seekStart()
                is SeekBarStopChangeEvent -> viewModel.seekStop(event.view.progress.toLong())
                is SeekBarProgressChangeEvent -> if (event.fromUser) viewModel.seek(event.progress.toLong())
            }
        }
    }

    override fun bindViewModel() {
        viewModel.previewAlphaState.observe {
            binding.constraintLayoutPreview.alpha = it
        }

        viewModel.previewAlphaState.observe {
            binding.constraintLayoutPreview.alpha = it
        }

        viewModel.playerState.observe { state ->
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

        viewModel.messageState.observe {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}