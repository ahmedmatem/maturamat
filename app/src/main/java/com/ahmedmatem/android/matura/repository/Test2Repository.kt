package com.ahmedmatem.android.matura.repository

import android.util.Log
import com.ahmedmatem.android.matura.datasource.local.Test2LocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.Test2RemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.java.KoinJavaComponent.inject

class Test2Repository {
    private val localDataSource: Test2LocalDataSource by inject(Test2LocalDataSource::class.java)
    private val remoteDataSource: Test2RemoteDataSource by inject(Test2RemoteDataSource::class.java)

    fun createTest() : Flow<Result<Test2>> = remoteDataSource.createTest2()

    fun getMockTest() : Flow<Result<Test2>> = remoteDataSource.getMockTest()

    fun getAll() = localDataSource.getAll()

    fun getTest2ById(testId: String) : Flow<Test2> = localDataSource.getTest2ById(testId)

    fun getProblemSolutions(testId: String, problemNumber: Int) : Flow<String?> =
        localDataSource.getTest2ById(testId).map {
            when(problemNumber) {
                1 -> it.firstSolutions
                2 -> it.secondSolutions
                3 -> it.thirdSolutions
                else -> null
            }
        }

    suspend fun updateSolutions(
        testId: String,
        problemNumber: Int,
        photoUri: String,
        currentSolutions: String?) {

        val solutions = ((currentSolutions ?: "") + ",$photoUri").trim(',')
        when (problemNumber) {
            1 -> localDataSource.updateFirstSolution(testId, solutions)
            2 -> localDataSource.updateSecondSolution(testId, solutions)
            3 -> localDataSource.updateThirdSolution(testId, solutions)
            else -> Log.e(TAG, "saveSolution: Error(Invalid problem number $problemNumber)")
        }
    }

    suspend fun insert(vararg tests: Test2) {
        localDataSource.insert(*tests)
    }

    companion object {
        const val TAG = "Test2Repository"
    }
}