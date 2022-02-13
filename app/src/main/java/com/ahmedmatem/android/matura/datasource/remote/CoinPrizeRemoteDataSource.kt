package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.CoinPrizeApiService
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent

class CoinPrizeRemoteDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val coinPrizeApiService: CoinPrizeApiService by lazy { PrizeApi.coinPrizeRetrofitService }

    private val _userPrefs: UserPrefs by KoinJavaComponent.inject(UserPrefs::class.java)
    private val username by lazy { _userPrefs.getUser()?.username }
    private val uuid by lazy { _userPrefs.getUuid() }
    private val usernameOrUuid by lazy { username ?: uuid }

    suspend fun getPrize(): Result<CoinPrize> {
        return safeApiCall(dispatcher) {
            coinPrizeApiService.getPrize(usernameOrUuid)
        }
    }

    suspend fun sync(prize: CoinPrize): Boolean = update(prize) is Result.Success

    private suspend fun update(prize: CoinPrize): Result<Unit> {
        return safeApiCall(dispatcher) {
            coinPrizeApiService.updatePrize(prize)
        }
    }
}