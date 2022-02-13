package com.ahmedmatem.android.matura.repository

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.datasource.local.PrizeLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.PrizeRemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Prize

interface IPrizeRepository<T : IPrizeItem> {
    val prizeLocalDataSource: PrizeLocalDataSource<T>
    val prizeRemoteDataSource: PrizeRemoteDataSource<T>

    fun getPrizeItem(): LiveData<T>?

    suspend fun getPrize(): Prize<T>?

    suspend fun sync(prize: Prize<T>)

    suspend fun update(prize: Prize<T>, synced: Boolean = false)

    suspend fun getPrizeRemote(): Result<Prize<T>>
}