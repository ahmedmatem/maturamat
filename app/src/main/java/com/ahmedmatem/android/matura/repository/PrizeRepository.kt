package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.network.services.PrizeApiService
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class PrizeRepository(
    context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val prizeLocalDataSource by lazy { MaturaDb.getInstance(context).prizeDao }
    private val prizeRemoteDataSource: PrizeApiService by lazy { PrizeApi.retrofitService }
    private val username by lazy { _userPrefs.getUser()?.username }

    fun getCoin(): LiveData<Coin>? {
        username?.let {
            return prizeLocalDataSource.getCoin(username!!)
        }
        return null
    }

    suspend fun getPrize(): Prize? {
        return withContext(dispatcher) {
            prizeLocalDataSource.getPrize(username!!)
        }
    }

    suspend fun syncPrize(prize: Prize) {
        withContext(dispatcher) {
            val synced = updatePrizeRemote(prize) is Result.Success
            update(prize, synced)
        }
    }

    suspend fun update(prize: Prize, synced: Boolean = false) {
        prize.prize.synced = synced
        upsertPrize(prize.prize, prize.period)
    }

    suspend fun getPrizeFromNetwork(): Result<Prize> {
        return safeApiCall(dispatcher) {
            prizeRemoteDataSource.getPrize(username!!)
        }
    }

    private suspend fun updatePrizeRemote(prize: Prize): Result<Unit> {
        return withContext(dispatcher) {
            safeApiCall(dispatcher) {
                prizeRemoteDataSource.updatePrize(prize)
            }
        }
    }

    private suspend fun upsertPrize(coin: Coin, period: Period) {
        prizeLocalDataSource.insertCoin(coin)
        prizeLocalDataSource.insertPeriod(period)
    }
}