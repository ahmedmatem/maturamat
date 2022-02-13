package com.ahmedmatem.android.matura.datasource.local

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class CoinPrizeLocalDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val coinPrizeDao by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.coinPrizeDao
    }

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val username by lazy { _userPrefs.getUser()?.username }
    private val uuid by lazy { _userPrefs.getUuid() }
    private val usernameOrUuid by lazy { username ?: uuid }

    fun getCoin(): LiveData<Coin>? {
        return coinPrizeDao.getCoin(usernameOrUuid)
    }

    suspend fun getPrize(): CoinPrize? {
        return withContext(dispatcher) {
            coinPrizeDao.getPrize(usernameOrUuid)
        }
    }

    suspend fun sync(prize: CoinPrize, synced: Boolean) = update(prize, synced)

    suspend fun update(prize: CoinPrize, synced: Boolean = false) {
        prize.coin.synced = synced
        upsert(prize.coin, prize.period)
    }

    private suspend fun upsert(coin: Coin, prizePeriod: Period) {
        coinPrizeDao.insertPrize(coin)
        coinPrizeDao.insertPeriod(prizePeriod)
    }
}