package com.ahmedmatem.android.matura.repository

import android.content.Context
import android.util.Log
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.addUsername
import com.ahmedmatem.android.matura.network.safeApiCall
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestRepository(
    val context: Context,
    private val database: MaturaDb,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val testApiService = TestApi.retrofitService
    private val username: String? by lazy { UserPrefs(context).getUser() }

    val testList by lazy { database.testDao.getAllBy(username!!) }

    suspend fun refreshTestList() {
        withContext(dispatcher) {
            username?.let {
                val token = database.tokenDao.getToken(it)
                val authorization = context.getString(R.string.authorization, token)
                val response = safeApiCall(dispatcher) {
                    testApiService.getAllTestByUser(authorization)
                }
                when (response) {
                    is Result.Success -> {
                        val tests = response.data.addUsername(username).toTypedArray()
                        database.testDao.insert(*tests)
                    }
                    is Result.GenericError -> {
                        Log.d(
                            "DEBUG",
                            "refreshTestList: Generis error (${response.errorResponse?.description})"
                        )
                    }
                    is Result.NetworkError -> {
                        Log.d("DEBUG", "refreshTestList: Network error")
                    }
                }
            }
        }
    }
}