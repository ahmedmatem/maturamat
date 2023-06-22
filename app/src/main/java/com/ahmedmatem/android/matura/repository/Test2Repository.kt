package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.local.Test2LocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.Test2RemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class Test2Repository {
    private val localDataSource: Test2LocalDataSource by inject(Test2LocalDataSource::class.java)
    private val remoteDataSource: Test2RemoteDataSource by inject(Test2RemoteDataSource::class.java)

    fun createTest() : Flow<Result<Test2>> = remoteDataSource.createTest2()

    fun getMockTest() : Flow<Result<Test2>> = remoteDataSource.getMockTest()

    suspend fun insert(vararg tests: Test2) {
        localDataSource.insert(*tests)
    }
}