package com.alex.mediacenter.feature.base.di

import com.alex.mediacenter.feature.base.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val resourceProviderModule = module {
    single { ResourceProvider(androidContext()) }
}