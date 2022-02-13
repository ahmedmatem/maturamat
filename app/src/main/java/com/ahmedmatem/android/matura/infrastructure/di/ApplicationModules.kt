package com.ahmedmatem.android.matura.infrastructure.di

import com.ahmedmatem.android.matura.datasource.local.PrizeLocalDataSource
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.PrizeRemoteDataSource
import com.ahmedmatem.android.matura.datasource.remote.TestRemoteDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.prizesystem.PrizeManager
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.repository.PrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.helpers.NoticeDataCreator
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.single

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
    single { PrizeLocalDataSource<IPrizeItem>() }
    single { TestLocalDataSource() }

    // remote
    single { PrizeRemoteDataSource<IPrizeItem>() }
    single { TestRemoteDataSource() }

    /**
     * Repositories
     */
    single { PrizeRepository<IPrizeItem>() }
    single { TestRepository(get(), get()) }

    single { TestURLUtil(get()) }
    single { SharedPreferencesProvider(get()) }
    single { ResourcesProvider(get()) }
    single { NoticeDataCreator(get()) }
    single { MaturaDb.getInstance(get()) }

    single { PrizeManager(androidContext()) }
}