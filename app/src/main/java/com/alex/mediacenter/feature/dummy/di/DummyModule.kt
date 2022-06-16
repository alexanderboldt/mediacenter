package com.alex.mediacenter.feature.dummy.di

import com.alex.mediacenter.feature.dummy.DummyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dummyModule = module {
    viewModel { DummyViewModel(get(), get()) }
}