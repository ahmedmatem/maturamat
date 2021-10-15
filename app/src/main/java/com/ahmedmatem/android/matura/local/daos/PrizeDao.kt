package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Prize

@Dao
interface PrizeDao {

    /**
     * Result of this query contains user coins and period they belong to (prize)
     * It's result of joining of two tables.
     */
    @Transaction
    @Query("SELECT * FROM coin_table WHERE holder = :username")
    suspend fun getPrizeForUser(username: String): Prize?

    @Query("SELECT * FROM coin_table WHERE holder = :username")
    suspend fun getCoinForUser(username: String): Coin?
}