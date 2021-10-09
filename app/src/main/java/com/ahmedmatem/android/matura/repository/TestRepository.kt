package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.safeApiCall
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestRepository(
    val context: Context,
    private val database: MaturaDb,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val testApiService = TestApi.retrofitService
    private val username: String? by lazy {
        val userPrefs = UserPrefs(context)
        return@lazy userPrefs.getUser()
    }

    val testList = if (username != null) {
        database.testDao.getAllByUser()
    } else {
        database.testDao.getAllByGuest()
    }

    suspend fun refreshTestList() {
        val response = if (username == null) {
            // Guest
//            refreshGuestTestList("")
            TODO("Not implemented yet")
        } else {
            // User
            refreshUserTestList(username!!)
        }
        when (response) {
            is Result.Success -> {
                val tests = response.data.toTypedArray()
                database.testDao.insert(*tests) // upsert data
            }
            is Result.GenericError -> TODO("Not implemented yet")
            is Result.NetworkError -> TODO("Not implemented yet")
        }
    }

    private suspend fun refreshUserTestList(username: String): Result<List<Test>> {
        val token = database.tokenDao.getToken(username)
        val authorization = context.getString(R.string.authorization, token)
        return safeApiCall(dispatcher) {
            testApiService.getAllTestByUser(authorization)
        }
    }

    private suspend fun refreshGuestTestList(uuid: String): Result<List<Test>> {
        TODO("Not implemented yet")
//        withContext(Dispatchers.IO) {
//            val tests = testApiService.getAllTestByGuest(uuid).toTypedArray()
//            database.testDao.insert(*tests)
//        }
    }
}