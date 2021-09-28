package com.ahmedmatem.android.matura.repository

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestRepository {
    private val testApiService = TestApi.retrofitService

//    val tests: LiveData<List<Test>> = database.testDao.getGuestTests()
    val tests: LiveData<List<Test>>? = null

    suspend fun refreshGuestTestList(uuid: String){
        withContext(Dispatchers.IO){
            val tests = testApiService.getAllTestForGuest(uuid).toTypedArray()
//            database.testDao.insert(*tests)
        }
    }

    suspend fun refreshUserTestList(authString: String){
        withContext(Dispatchers.IO){
            val tests = testApiService.getAllTestByUser(authString).toTypedArray()
//            database.testDao.insert(*tests)
        }
    }
}