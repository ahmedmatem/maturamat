package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.local.CoinLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.CoinRemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class CoinRepository(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val localDataSource: CoinLocalDataSource by inject(CoinLocalDataSource::class.java)
    private val remoteDataSource: CoinRemoteDataSource by inject(CoinRemoteDataSource::class.java)

    /**
     * Get coin from local Db
     */
    suspend fun getCoin(): Coin? {
        return localDataSource.getCoin()
    }

    /**
     *  Update/Insert coin in local Db
     */
    suspend fun update(coin: Coin) {
        localDataSource.insert(coin)
    }

    suspend fun sync(coin: Coin) {
        remoteDataSource.sync(coin)
        localDataSource.sync(coin)
    }

    suspend fun syncRemote(coin: Coin): Boolean {
        return remoteDataSource.sync(coin)
    }

    suspend fun getCoinRemote(): Coin? {
        return when (val response = remoteDataSource.getCoin()) {
            is Result.Success -> response.data
            else -> null
        }
    }
}