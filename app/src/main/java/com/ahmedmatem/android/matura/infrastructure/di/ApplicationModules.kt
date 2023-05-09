package com.ahmedmatem.android.matura.infrastructure.di

import androidx.work.WorkManager
import com.ahmedmatem.android.matura.datasource.local.AccountLocalDataSource
import com.ahmedmatem.android.matura.datasource.local.CoinLocalDataSource
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.AccountRemoteDataSource
import com.ahmedmatem.android.matura.datasource.remote.CoinRemoteDataSource
import com.ahmedmatem.android.matura.datasource.remote.TestRemoteDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.services.TestApi
import com.ahmedmatem.android.matura.prizesystem.PrizeWorkManager
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.repository.CoinRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.helpers.NoticeDataCreator
import com.ahmedmatem.android.matura.utils.providers.ContextProvider
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
import org.koin.dsl.module

val applicationModule = module {

    single { UserPrefs(get()) }

    /**
     * Repositories
     */

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
    single { CoinLocalDataSource() }
    single { TestLocalDataSource() }
    single { AccountLocalDataSource() }

    // remote
    single { CoinRemoteDataSource() }
    single { TestRemoteDataSource() }
    single { AccountRemoteDataSource() }

    // Repositories
    single { CoinRepository() }
    single { TestRepository() }

    single { TestURLUtil(get()) }
    single { SharedPreferencesProvider(get()) }
    single { ResourcesProvider(get()) }
    single { NoticeDataCreator(get()) }
    single { MaturaDb.getInstance(get()) }

    single { PrizeWorkManager(get()) }
    single { WorkManager.getInstance(get()) }

    // Providers
    single { ContextProvider(get()) }

}