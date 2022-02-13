package com.ahmedmatem.android.matura.repository

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.datasource.local.PrizeLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.PrizeRemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PrizeRepository<T : IPrizeItem>(dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    IPrizeRepository<T> {
    
    override val prizeLocalDataSource = PrizeLocalDataSource<T>(dispatcher)
    override val prizeRemoteDataSource = PrizeRemoteDataSource<T>(dispatcher)

    override fun getPrizeItem(): LiveData<T>? {
        return prizeLocalDataSource.getPrizeItem()
    }

    override suspend fun getPrize(): Prize<T>? {
        return prizeLocalDataSource.getPrize()
    }

    override suspend fun sync(prize: Prize<T>) {
        val synced = prizeRemoteDataSource.sync(prize)
        prizeLocalDataSource.sync(prize, synced)
    }

    override suspend fun update(prize: Prize<T>, synced: Boolean) {
        prizeLocalDataSource.update(prize, synced)
    }

    override suspend fun getPrizeRemote(): Result<Prize<T>> {
        return prizeRemoteDataSource.getPrize()
    }
}