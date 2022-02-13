package com.ahmedmatem.android.matura.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.Prize

@Dao
interface CoinPrizeDao {

    /**
     * Result of this query contains user coins and period they belong to (prize)
     * It's result of joining of two tables.
     */
    @Transaction
    @Query("SELECT * FROM coin_table WHERE holder = :username")
    suspend fun getPrize(username: String): Prize<Coin>?

    @Query("SELECT * FROM coin_table WHERE holder = :username")
    fun getCoin(username: String): LiveData<Coin>?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrize(coin: Coin, period: Period)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriod(period: Period)
}