package com.ahmedmatem.android.matura.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.Flow

@Dao
interface Test2Dao {

    @Query("SELECT * FROM test2 WHERE id = :id")
    fun getTestById(id: String) : Test2?

    @Query("UPDATE test2 SET solution_1 = :solutions WHERE id = :testId")
    suspend fun updateFirstSolution(testId: String, solutions: String)

    @Query("UPDATE test2 SET solution_2 = :solutions WHERE id = :testId")
    suspend fun updateSecondSolution(testId: String, solutions: String)

    @Query("UPDATE test2 SET solution_3 = :solutions WHERE id = :testId")
    suspend fun updateThirdSolution(testId: String, solutions: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tests: Test2)

    @Delete
    fun delete(vararg tests: Test2)
}