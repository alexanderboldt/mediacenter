package com.alex.mediacenter.feature.dummy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.alex.mediacenter.databinding.ControllerDummyBinding
import com.alex.mediacenter.feature.base.BaseFragment
import com.alex.mediacenter.util.getStatusBarHeight
import org.koin.android.ext.android.inject

class DummyFragment : BaseFragment<ControllerDummyBinding>() {

    private val viewModel: DummyViewModel by inject()

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): ControllerDummyBinding {
        return ControllerDummyBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.root.updatePadding(top = resources.getStatusBarHeight())
    }

    override fun setupViewBinding() {
        binding.button3.setOnClickListener {
            viewModel.clickOnReleasePlayer()
        }

        binding.button.setOnClickListener {
            viewModel.clickOnButtonOne()
        }

        binding.button2.setOnClickListener {
            viewModel.clickOnButtonTwo()
        }
    }
}