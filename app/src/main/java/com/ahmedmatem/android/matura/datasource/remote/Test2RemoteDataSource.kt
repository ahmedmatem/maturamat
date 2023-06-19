package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.infrastructure.BASE_API_URL
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.network.services.Test2Api
import com.ahmedmatem.android.matura.network.services.Test2ApiService
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Test2RemoteDataSource {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val apiService: Test2ApiService = Test2Api.retrofitService

    fun createTest2() : Flow<Result<Test2>> = flow {
        val result = safeApiCall(dispatcher) {
            apiService.createTest2()
        }
        emit(result)
    }

    fun getMockTest() : Flow<Result<Test2>> = flow {
        val result = safeApiCall(dispatcher) {
            apiService.getMockTest()
        }
        emit(result)
    }
}