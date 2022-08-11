package com.alex.mediacenter.feature.base.di

import com.alex.mediacenter.feature.base.ResourceProvider
import com.alex.mediacenter.feature.base.WidgetManager
import com.alexstyl.warden.Warden
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {
    single { ResourceProvider(androidContext()) }
    single { WidgetManager(androidContext()) }
    factory { Warden.with(androidContext()) }
}