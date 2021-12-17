package com.ahmedmatem.android.matura.infrastructure.di

import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {

    single { UserPrefs(androidContext()) }

    single {
        AccountRepository(
            MaturaDb.getInstance(get()).accountDao,
            AccountApi.retrofitService
        )
    }

}