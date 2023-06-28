package com.ahmedmatem.android.matura.repository

import android.util.Log
import com.ahmedmatem.android.matura.datasource.local.Test2LocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.Test2RemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.koin.java.KoinJavaComponent.inject

class Test2Repository {
    private val localDataSource: Test2LocalDataSource by inject(Test2LocalDataSource::class.java)
    private val remoteDataSource: Test2RemoteDataSource by inject(Test2RemoteDataSource::class.java)

    fun createTest() : Flow<Result<Test2>> = remoteDataSource.createTest2()

    fun getMockTest() : Flow<Result<Test2>> = remoteDataSource.getMockTest()

    suspend fun updateSolution(testId: String, problemNumber: Int, photoUri: String) {
        /** First get test with testId from database */
        localDataSource.getTestById(testId).collect {
            it?.let { test ->
                // if test exists in database
                when (problemNumber) {
                    1 -> {
                        val solutions = ((test.firstSolutions ?: "") + ",$photoUri").trim(',')
                        localDataSource.updateFirstSolution(testId, solutions)
                    }
                    2 -> {
                        val solutions = ((test.secondSolutions ?: "") + ",$photoUri").trim(',')
                        localDataSource.updateSecondSolution(testId, solutions)
                    }
                    3 -> {
                        val solutions = ((test.thirdSolutions ?: "") + ",$photoUri").trim(',')
                        localDataSource.updateThirdSolution(testId, solutions)
                    }
                    else -> Log.e(TAG, "saveSolution: Error(Invalid problem number $problemNumber)")
                }
            }
        }
    }

    suspend fun insert(vararg tests: Test2) {
        localDataSource.insert(*tests)
    }

    companion object {
        const val TAG = "Test2Repository"
    }
}