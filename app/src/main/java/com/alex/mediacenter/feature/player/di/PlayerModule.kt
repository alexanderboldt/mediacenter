package com.alex.mediacenter.feature.player.di

import com.alex.mediacenter.feature.player.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    viewModel { PlayerViewModel(get(), get()) }
}