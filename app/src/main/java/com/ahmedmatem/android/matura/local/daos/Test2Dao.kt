package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ahmedmatem.android.matura.network.models.Test2

@Dao
interface Test2Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tests: Test2)

    @Delete
    fun delete(vararg tests: Test2)
}