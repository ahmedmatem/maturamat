package com.ahmedmatem.android.matura.infrastructure.di

import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.prizesystem.PrizeManager
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.repository.PrizeRepository
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
import kotlinx.coroutines.Dispatchers
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

    single {
        PrizeRepository(
            androidContext(),
            dispatcher = Dispatchers.IO
        )
    }

    single { TestURLUtil(get()) }
    single { SharedPreferencesProvider(get()) }
    single { ResourcesProvider(get()) }

    single { PrizeManager(androidContext()) }
}