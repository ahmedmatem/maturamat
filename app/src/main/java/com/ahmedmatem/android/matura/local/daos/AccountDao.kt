package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmedmatem.android.matura.network.models.Token

@Dao
interface AccountDao {

    @Query("SELECT token FROM token_table WHERE user_name = :username LIMIT 1")
    fun getToken(username: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: Token)

    @Query("SELECT EXISTS(SELECT * FROM token_table WHERE user_name = :email)")
    suspend fun isUserExists(email: String): Boolean

    @Query("SELECT * FROM token_table WHERE user_name = :userName")
    suspend fun getUser(userName: String): Token?
}