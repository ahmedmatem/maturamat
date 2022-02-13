package com.ahmedmatem.android.matura.repository

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.datasource.local.CoinPrizeLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.CoinPrizeRemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class CoinPrizeRepository(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val coinPrizeLocalDataSource: CoinPrizeLocalDataSource by inject(
        CoinPrizeLocalDataSource::class.java
    )
    private val coinPrizeRemoteDataSource: CoinPrizeRemoteDataSource by inject(
        CoinPrizeRemoteDataSource::class.java
    )

    fun getCoin(): LiveData<Coin>? {
        return coinPrizeLocalDataSource.getCoin()
    }

    suspend fun getPrize(): CoinPrize? {
        return coinPrizeLocalDataSource.getPrize()
    }

    suspend fun sync(prize: CoinPrize) {
        val synced = coinPrizeRemoteDataSource.sync(prize)
        coinPrizeLocalDataSource.sync(prize, synced)
    }

    suspend fun update(prize: CoinPrize, synced: Boolean = false) {
        coinPrizeLocalDataSource.update(prize, synced)
    }

    suspend fun getPrizeRemote(): Result<CoinPrize> {
        return coinPrizeRemoteDataSource.getPrize()
    }
}