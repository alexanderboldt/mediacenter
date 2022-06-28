package com.alex.mediacenter.feature.selector.di

import com.alex.mediacenter.feature.selector.SelectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val selectorModule = module {
    viewModel { SelectorViewModel(get()) }
}