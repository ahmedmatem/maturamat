package com.ahmedmatem.android.matura.repository

import android.content.Context
import com.ahmedmatem.android.matura.local.MaturaDb
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

    suspend fun getCoinForUser(): Int {
        return withContext(dispatcher) {
            val coin = prizeDao.getCoinForUser(username)
            coin?.let {
                coin.gift + coin.earned
            }
            0
        }
    }

    suspend fun getPrizeForUser(): Prize? {
        return withContext(dispatcher) {
            prizeDao.getPrizeForUser(username)
        }
    }
}