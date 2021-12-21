package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmedmatem.android.matura.network.models.User

@Dao
interface AccountDao {

    @Query("SELECT token FROM user_table WHERE user_name = :username LIMIT 1")
    fun getToken(username: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE user_name = :userName)")
    suspend fun isUserExists(userName: String): Boolean

    @Query("SELECT * FROM user_table WHERE user_name = :userName")
    suspend fun getUser(userName: String): User?
}