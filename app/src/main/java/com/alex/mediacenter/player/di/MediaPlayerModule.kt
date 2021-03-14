package com.alex.mediacenter.player.di

import com.alex.mediacenter.player.MediaPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val mediaPlayerModule = module {
    single { MediaPlayer(androidApplication()) }
}