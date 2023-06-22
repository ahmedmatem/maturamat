package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ahmedmatem.android.matura.network.models.Test2

@Dao
interface Test2Dao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg tests: Test2)
}