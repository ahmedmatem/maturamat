package com.ahmedmatem.android.matura.infrastructure.di

import com.ahmedmatem.android.matura.datasource.local.CoinPrizeLocalDataSource
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.CoinPrizeRemoteDataSource
import com.ahmedmatem.android.matura.datasource.remote.TestRemoteDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.prizesystem.PrizeManager
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.helpers.NoticeDataCreator
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
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

    /**
     * Data sources
     */

    // local
    single { CoinPrizeLocalDataSource() }
    single { TestLocalDataSource() }

    // remote
    single { CoinPrizeRemoteDataSource() }
    single { TestRemoteDataSource() }

    /**
     * Repositories
     */
    single { CoinPrizeRepository() }
    single { TestRepository() }

    single { TestURLUtil(get()) }
    single { SharedPreferencesProvider(get()) }
    single { ResourcesProvider(get()) }
    single { NoticeDataCreator(get()) }
    single { MaturaDb.getInstance(get()) }

    single { PrizeManager(androidContext()) }
}