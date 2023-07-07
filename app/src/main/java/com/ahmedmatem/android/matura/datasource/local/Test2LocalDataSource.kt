package com.ahmedmatem.android.matura.datasource.local

import com.ahmedmatem.android.matura.local.daos.Test2Dao
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.java.KoinJavaComponent.inject

class Test2LocalDataSource {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val dataSource: Test2Dao by inject(Test2Dao::class.java)

    fun getAll() : Flow<List<Test2>> = dataSource.getAll().flowOn(ioDispatcher)

    fun getTest2ById(testId: String) : Flow<Test2> = dataSource.getTest2ById(testId)
        .flowOn(ioDispatcher)

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