package com.ahmedmatem.android.matura.repository

import android.content.Context
import com.ahmedmatem.android.matura.local.MaturaDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrizeRepository(
    context: Context,
    private val username: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val prizeDao by lazy { MaturaDb.getInstance(context).prizeDao }

    suspend fun getCoinsForUser(): Int {
        return withContext(dispatcher) {
            val coin = prizeDao.getCoinsForUser(username)
            val totalCoin = coin.default + coin.earned
            totalCoin
        }
    }
}