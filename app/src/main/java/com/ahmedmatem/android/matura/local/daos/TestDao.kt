package com.ahmedmatem.android.matura.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedmatem.android.matura.network.models.Test

@Dao
interface TestDao {

    @Query("SELECT * FROM test_table WHERE username = :username")
    fun getTestListByUsername(username: String?): LiveData<List<Test>>

    @Query("SELECT * FROM test_table WHERE uuid = :uuid")
    fun getTestListByUuid(uuid: String?): LiveData<List<Test>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tests: Test)

    @Delete
    suspend fun delete(vararg tests: Test)
}