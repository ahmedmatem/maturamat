package com.ahmedmatem.android.matura.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedmatem.android.matura.network.models.Test

@Dao
interface TestDao {

    @Query("SELECT * FROM test_table WHERE username = :username AND level = :level ORDER BY created_on DESC")
    fun getTestListByUsername(username: String?, level: Int): LiveData<List<Test>>

    @Query("SELECT * FROM test_table WHERE uuid = :uuid AND level = :level ORDER BY created_on DESC")
    fun getTestListByUuid(uuid: String?, level: Int): LiveData<List<Test>>

    @Query("SELECT EXISTS(SELECT * FROM test_table WHERE username = :username LIMIT 1)")
    suspend fun isEmptyUserTestList(username: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tests: Test)

    @Delete
    suspend fun delete(vararg tests: Test)
}