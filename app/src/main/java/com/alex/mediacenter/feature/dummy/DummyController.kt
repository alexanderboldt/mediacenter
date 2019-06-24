package com.alex.mediacenter.feature.dummy

import com.alex.mediacenter.R
import com.alex.mediacenter.databinding.ControllerDummyBinding
import com.alex.mediacenter.feature.base.BaseController
import com.alex.mediacenter.util.plusAssign
import com.jakewharton.rxbinding2.view.clicks

class DummyController : BaseController<ControllerDummyBinding>(R.layout.controller_dummy) {

    private val viewModel by lazy { viewModelProvider().get(DummyViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onSetupViewBinding() {
        disposables += binding.button.clicks().subscribe {
            viewModel.clickOnButtonOne()
        }

        disposables += binding.button2.clicks().subscribe {
            viewModel.clickOnButtonTwo()
        }
    }
}