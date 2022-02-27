package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.CoinApiService
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class CoinRemoteDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val coinApiService: CoinApiService by lazy { PrizeApi.coinRetrofitService }

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val authorizationHeader: String by lazy {
        _userPrefs.getUser()?.let {
            "Bearer ${it.token}"
        } ?: ""
    }

    suspend fun getCoin(): Result<Coin> {
        return safeApiCall(dispatcher) {
            coinApiService.getCoin(authorizationHeader)
        }
    }

    suspend fun sync(coin: Coin): Boolean = update(coin) is Result.Success

    private suspend fun update(coin: Coin): Result<Unit> {
        return safeApiCall(dispatcher) {
            coinApiService.updateCoin(authorizationHeader, coin)
        }
    }

    companion object {
        const val TAG = "PrizeRemoteDataSource"
    }
}