package com.ahmedmatem.android.matura.datasource.remote

import android.util.Log
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.addUsername
import com.ahmedmatem.android.matura.network.models.addUuid
import com.ahmedmatem.android.matura.network.services.TestApi
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class TestRemoteDataSource {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val testApiService = TestApi.retrofitService

    suspend fun getAllForUser(token: String): List<Test>? {
        val authorization = "bearer $token"
        val response = safeApiCall(dispatcher) {
            testApiService.getAllTestsByUser(authorization)
        }

        return when (response) {
            is Result.Success -> {
                response.data
            }
            is Result.GenericError -> {
                Log.d(
                    "DEBUG",
                    "getTestListForUser: Generic error (${response.errorResponse?.description})"
                )
                null
            }
            is Result.NetworkError -> {
                Log.d("DEBUG", "getTestListForUser: Network error")
                null
            }
        }
    }

    suspend fun getAllForGuest(uuid: String): List<Test>? {
        val response = safeApiCall(dispatcher) {
            testApiService.getAllTestsByGuest(uuid)
        }

        return when (response) {
            is Result.Success -> {
                response.data
            }
            is Result.GenericError -> {
                Log.d(
                    "DEBUG",
                    "getTestListForUser: Generic error (${response.errorResponse?.description})"
                )
                null
            }
            is Result.NetworkError -> {
                Log.d("DEBUG", "getTestListForUser: Network error")
                null
            }
        }
    }

    suspend fun getLastTestsForUser(token: String, count: Int = 1): List<Test>? {
        val authorization = "bearer $token"
        val response = safeApiCall(dispatcher) {
            testApiService.getLastTestsByUser(authorization, count)
        }

        return when (response) {
            is Result.Success -> {
                response.data
            }
            is Result.GenericError -> {
                Log.d(
                    "DEBUG",
                    "getLastTestForUser: Generic error (${response.errorResponse?.description})"
                )
                null
            }
            is Result.NetworkError -> {
                Log.d("DEBUG", "getLastTestForUser: Network error")
                null
            }
        }
    }

    suspend fun getLastTestsForGuest(uuid: String, count: Int = 1): List<Test>? {
        var response = safeApiCall(dispatcher) {
            testApiService.getLastTestsByGuest(uuid!!, count)
        }

        return when (response) {
            is Result.Success -> {
                response.data
            }
            is Result.GenericError -> {
                Log.d(
                    "DEBUG",
                    "getTestListForUser: Generic error (${response.errorResponse?.description})"
                )
                null
            }
            is Result.NetworkError -> {
                Log.d("DEBUG", "getTestListForUser: Network error")
                null
            }
        }
    }

    suspend fun getTestById(testId: String): Test? {
        val response = safeApiCall(dispatcher) {
            testApiService.getById(testId)
        }

        return when (response) {
            is Result.Success -> {
                response.data
            }
            is Result.GenericError -> {
                Log.d(
                    "DEBUG",
                    "getTestById: Generic error (${response.errorResponse?.description})"
                )
                null
            }
            is Result.NetworkError -> {
                Log.d("DEBUG", "getTestById: Network error")
                null
            }
        }
    }
}