package com.ahmedmatem.android.matura.local.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.ahmedmatem.android.matura.prizesystem.contract.IPrize
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.Prize

interface IPrizeDao<T: IPrize> {
    @Transaction
    suspend fun getPrize(username: String): Prize<T>?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrize(prize: T, period: Period)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrize(prize: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriod(period: Period)

}