package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestRepository(val applicationContext: Context, private val database: MaturaDb) {
    private val testApiService = TestApi.retrofitService

    val guestTests: LiveData<List<Test>> = database.testDao.getAllByGuest()
    val userTests: LiveData<List<Test>> = database.testDao.getAllByUser()

    suspend fun refreshGuestTestList(uuid: String) {
        withContext(Dispatchers.IO) {
            val tests = testApiService.getAllTestByGuest(uuid).toTypedArray()
            database.testDao.insert(*tests)
        }
    }

    suspend fun refreshUserTestList(authString: String) {
        withContext(Dispatchers.IO) {
            val tests = testApiService.getAllTestByUser(authString).toTypedArray()
            database.testDao.insert(*tests)
        }
    }
}