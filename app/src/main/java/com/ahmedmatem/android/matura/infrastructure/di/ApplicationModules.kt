package com.ahmedmatem.android.matura.infrastructure.di

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {
    single { UserPrefs(androidContext()) }
}