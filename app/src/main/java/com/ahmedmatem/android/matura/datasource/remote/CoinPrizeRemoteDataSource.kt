package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.CoinPrizeModel
import com.ahmedmatem.android.matura.network.services.CoinPrizeApiService
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent

class CoinPrizeRemoteDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val coinPrizeApiService: CoinPrizeApiService by lazy { PrizeApi.coinPrizeRetrofitService }

    private val _userPrefs: UserPrefs by KoinJavaComponent.inject(UserPrefs::class.java)
    private val authorizationHeader: String by lazy {
        _userPrefs.getUser()?.let {
            "Bearer ${it.token}"
        } ?: ""
    }

    suspend fun getPrize(): Result<CoinPrizeModel> {
        return safeApiCall(dispatcher) {
            coinPrizeApiService.getPrize(authorizationHeader)
        }
    }

    suspend fun sync(prize: CoinPrizeModel): Boolean = update(prize) is Result.Success

    private suspend fun update(prize: CoinPrizeModel): Result<Unit> {
        return safeApiCall(dispatcher) {
            coinPrizeApiService.updatePrize(authorizationHeader, prize)
        }
    }
}