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

class TestRemoteDataSource(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val testApiService = TestApi.retrofitService

    private val localDb: MaturaDb by inject(MaturaDb::class.java)
    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)

    private val username: String? by lazy { userPrefs.getUser()?.username }
    private val uuid: String = userPrefs.getUuid()

    /**
     * Get test list for user (if such logged in) or guest(not logged in user)
     */
    suspend fun refreshTestList() {
        withContext(dispatcher) {
            var response = if (username != null) { /* User logged in */
                val token = localDb.accountDao.getToken(username!!)
                val authorization = "bearer $token"
                safeApiCall(dispatcher) {
                    testApiService.getAllTestByUser(authorization)
                }
            } else { /* In case of guest */
                safeApiCall(dispatcher) {
                    testApiService.getAllTestByGuest(uuid)
                }
            }

            when (response) {
                is Result.Success -> {
                    var tests = response.data
                    if (username != null) {
                        tests.addUsername(username)
                    } else {
                        tests.addUuid(uuid)
                    }
                    localDb.testDao.insert(*tests.toTypedArray())
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