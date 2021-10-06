package com.ahmedmatem.android.matura.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmedmatem.android.matura.network.models.Token

@Dao
interface AuthDao {
    @Query("SELECT * FROM token_table WHERE user_name = :userName")
    fun getToken(userName: String): LiveData<Token>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: Token)
}