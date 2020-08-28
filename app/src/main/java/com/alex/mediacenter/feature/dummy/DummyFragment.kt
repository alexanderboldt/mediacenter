package com.alex.mediacenter.feature.dummy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.alex.mediacenter.databinding.ControllerDummyBinding
import com.alex.mediacenter.feature.base.BaseFragment
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding4.view.clicks
import org.koin.android.ext.android.inject

class DummyFragment : BaseFragment<ControllerDummyBinding>() {

    private val viewModel: DummyViewModel by inject()

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): ControllerDummyBinding {
        return ControllerDummyBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.root.updatePadding(top = getStatusBarHeight())
    }

    override fun bindView() {
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