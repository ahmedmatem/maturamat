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

    private val localDataSource: CoinPrizeLocalDataSource by inject(
        CoinPrizeLocalDataSource::class.java
    )
    private val remoteDataSource: CoinPrizeRemoteDataSource by inject(
        CoinPrizeRemoteDataSource::class.java
    )

    fun getCoin(): LiveData<Coin>? {
        return localDataSource.getCoin()
    }

    suspend fun getPrize(): CoinPrize? {
        return localDataSource.getPrize()
    }

    suspend fun sync(prize: CoinPrize) {
        val synced = remoteDataSource.sync(prize)
        localDataSource.sync(prize, synced)
    }

    suspend fun update(prize: CoinPrize, synced: Boolean = false) {
        localDataSource.update(prize, synced)
    }

    suspend fun getPrizeRemote(): Result<CoinPrize> {
        return remoteDataSource.getPrize()
    }
}