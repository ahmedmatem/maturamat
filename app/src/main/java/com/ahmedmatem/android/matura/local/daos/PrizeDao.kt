package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ahmedmatem.android.matura.prizesystem.models.PrizeAndPeriod

@Dao
interface PrizeDao {
    @Transaction
    @Query("SELECT * FROM prize_table")
    fun getPrizeAndPeriod(): PrizeAndPeriod?
}