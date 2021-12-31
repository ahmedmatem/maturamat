package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.PrizeApi
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
    private val prizeDao by lazy { MaturaDb.getInstance(context).prizeDao }
    private val username by lazy { _userPrefs.getUser()?.username }

    fun getCoin(): LiveData<Coin> {
        return prizeDao.getCoinForUser(username!!)
    }

    suspend fun getPrize(): Prize? {
        return withContext(dispatcher) {
            prizeDao.getPrizeForUser(username!!)
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
            PrizeApi.retrofitService.getUserPrize(username!!)
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