package com.ahmedmatem.android.matura.local.daos

import androidx.room.*
import com.ahmedmatem.android.matura.prizesystem.models.Coin

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin_table WHERE holder = :username LIMIT 1")
    suspend fun getCoin(username: String): Coin?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)
}