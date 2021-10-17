package com.ahmedmatem.android.matura.repository

import android.content.Context
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.safeApiCall
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrizeRepository(
    context: Context,
    private val username: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val prizeDao by lazy { MaturaDb.getInstance(context).prizeDao }

    suspend fun getCoin(): Int {
        return withContext(dispatcher) {
            val coin = prizeDao.getCoinForUser(username)
            coin?.let {
                coin.gift + coin.earned
            }
            0
        }
    }

    suspend fun getPrize(): Prize? {
        return withContext(dispatcher) {
            prizeDao.getPrizeForUser(username)
        }
    }

    suspend fun syncPrize(prize: Prize) {
        withContext(dispatcher) {
            val synced = updatePrizeRemote(prize) is Result.Success
            update(prize, synced)
        }
    }

    suspend fun update(prize: Prize, synced: Boolean = false) {
        prize.coin.synced = synced
        upsertPrize(prize.coin, prize.period)
    }

    suspend fun getPrizeFromNetwork(): Result<Prize> {
        return safeApiCall(dispatcher) {
            PrizeApi.retrofitService.getUserPrize(username)
        }
    }

    private suspend fun updatePrizeRemote(prize: Prize): Result<Unit> {
        return withContext(dispatcher) {
            safeApiCall(dispatcher) {
                PrizeApi.retrofitService.updatePrize(prize)
            }
        }
    }

    private suspend fun upsertPrize(coin: Coin, period: Period) {
        prizeDao.insertCoin(coin)
        prizeDao.insertPeriod(period)
    }
}