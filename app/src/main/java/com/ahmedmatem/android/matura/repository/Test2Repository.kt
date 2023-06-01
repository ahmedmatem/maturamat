package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.remote.Test2RemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class Test2Repository {
    private val remoteDataSource: Test2RemoteDataSource by inject(Test2RemoteDataSource::class.java)

    fun createTest() : Flow<Result<Test2>> = remoteDataSource.createTest2()

    fun getMockTest() : Flow<Result<Test2>> = remoteDataSource.getMockTest()
}