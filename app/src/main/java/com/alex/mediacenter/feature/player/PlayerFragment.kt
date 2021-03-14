package com.alex.mediacenter.feature.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import coil.load
import coil.transform.BlurTransformation
import com.alex.mediacenter.databinding.ControllerPlayerBinding
import com.alex.mediacenter.feature.base.BaseFragment
import com.alex.mediacenter.util.getBottomBarHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject

class PlayerFragment : BaseFragment<ControllerPlayerBinding>() {

    private val viewModel: PlayerViewModel by inject()

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            viewModel.onBottomSheetOffset(slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {}
    }

    // ----------------------------------------------------------------------------

    override fun onDestroyView() {
        removeBottomSheetCallback(bottomSheetCallback)
        super.onDestroyView()
    }

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): ControllerPlayerBinding {
        return ControllerPlayerBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        addBottomSheetCallback(bottomSheetCallback)

        binding.constraintLayoutPlayer.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = resources.getBottomBarHeight()
        }
    }

    override fun setupViewBinding() {
        binding.constraintLayoutPreview.setOnClickListener {
            viewModel.onClickPreview()
        }

        binding.imageViewPreviewPlay.setOnClickListener {
            viewModel.onClickPlay()
        }

        binding.imageViewPlay.setOnClickListener {
            viewModel.onClickPlay()
        }

        binding.imageViewPreviewPause.setOnClickListener {
            viewModel.clickOnPause()
        }

        binding.imageViewPause.setOnClickListener {
            viewModel.clickOnPause()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekStop(seekBar!!.progress.toLong())
            }
        })
    }

    override fun setupViewModel() {
        viewModel.bottomSheetState.observe { state ->
            setBottomSheetState(state)
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
                binding.imageViewPreviewCover.load(state.coverUrl)
                binding.imageViewBackground.load(state.coverUrl) {
                    this.transformations(BlurTransformation(requireContext(), 10f, 10f))
                }
                binding.imageViewCover.load(state.coverUrl)
            }
        }

        viewModel.messageState.observe {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}