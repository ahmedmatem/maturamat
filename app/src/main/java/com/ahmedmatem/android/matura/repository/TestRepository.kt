package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.entities.TestEntity
import com.ahmedmatem.android.matura.network.models.toDatabaseModel
import com.ahmedmatem.android.matura.network.services.TestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestRepository(val applicationContext: Context, private val database: MaturaDb) {
    private val testApiService = TestApi.retrofitService

    val guestTests: LiveData<List<TestEntity>> = database.testDao.getAllByGuest()
    val userTests: LiveData<List<TestEntity>> = database.testDao.getAllByUser()

    suspend fun refreshGuestTestList(uuid: String) {
        withContext(Dispatchers.IO) {
            val tests =
                testApiService.getAllTestByGuest(uuid).toDatabaseModel(true).toTypedArray()
            database.testDao.insert(*tests)
        }
    }

    suspend fun refreshUserTestList(authString: String) {
        withContext(Dispatchers.IO) {
            val tests =
                testApiService.getAllTestByUser(authString).toDatabaseModel(false).toTypedArray()
            database.testDao.insert(*tests)
        }
    }
}