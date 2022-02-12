package com.ahmedmatem.android.matura.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.addUsername
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class TestRepository(
    val context: Context,
    private val database: MaturaDb,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val testApiService = TestApi.retrofitService // remote data source
    private val localDataSource: TestLocalDataSource by inject(TestLocalDataSource::class.java)

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val username: String? by lazy { userPrefs.getUser()?.username }

    //    val testList: LiveData<List<Test>> by lazy { database.testDao.getAllBy(username) }
    val testList: LiveData<List<Test>> by lazy {
        username?.let {
            localDataSource.getAll(it)
        } ?: localDataSource.getAll(userPrefs.getUuid())
    }

    suspend fun refreshTestList() {
        withContext(dispatcher) {
            username?.let {
                val token = database.accountDao.getToken(it)
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
                            "refreshTestList: Generic error (${response.errorResponse?.description})"
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