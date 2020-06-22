package com.alex.mediacenter.feature.dummy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.alex.mediacenter.databinding.ControllerDummyBinding
import com.alex.mediacenter.feature.base.BaseController
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding4.view.clicks

class DummyController : BaseController<ControllerDummyBinding>() {

    private val viewModel by lazy { getViewModel(DummyViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup): ControllerDummyBinding {
        return ControllerDummyBinding.inflate(inflater, container, false)
    }

    override fun onSetupView() {
        binding.root.updatePadding(top = getStatusBarHeight())
    }

    override fun onViewBinding() {
        disposables += binding.button3.clicks().subscribe {
            viewModel.clickOnReleasePlayer()
        }

        disposables += binding.button.clicks().subscribe {
            viewModel.clickOnButtonOne()
        }

        disposables += binding.button2.clicks().subscribe {
            viewModel.clickOnButtonTwo()
        }
    }
}