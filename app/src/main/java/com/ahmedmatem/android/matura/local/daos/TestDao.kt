package com.ahmedmatem.android.matura.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedmatem.android.matura.local.entities.TestEntity

@Dao
interface TestDao {
    @Query("SELECT * FROM test_table")
    fun getAll(): LiveData<List<TestEntity>>

    @Query("SELECT * FROM test_table WHERE is_guest")
    fun getAllForGuest(): LiveData<List<TestEntity>>

    @Query("SELECT * FROM test_table WHERE NOT is_guest")
    fun getAllForUser(): LiveData<List<TestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tests: TestEntity)

    @Delete
    suspend fun delete(vararg tests: TestEntity)
}