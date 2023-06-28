package com.ahmedmatem.android.matura.datasource.local

import androidx.room.Query
import com.ahmedmatem.android.matura.local.daos.Test2Dao
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

class Test2LocalDataSource {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val dataSource: Test2Dao by inject(Test2Dao::class.java)

    fun getTestById(testId: String) : Flow<Test2?> = flow {
        val test = dataSource.getTestById(testId)
        emit(test)
    }

    suspend fun updateFirstSolution(testId: String, solutions: String) {
        dataSource.updateFirstSolution(testId, solutions)
    }

    suspend fun updateSecondSolution(testId: String, solutions: String) {
        dataSource.updateSecondSolution(testId, solutions)
    }

    suspend fun updateThirdSolution(testId: String, solutions: String) {
        dataSource.updateThirdSolution(testId, solutions)
    }

    suspend fun insert(vararg tests: Test2) {
        dataSource.insert(*tests)
    }
}