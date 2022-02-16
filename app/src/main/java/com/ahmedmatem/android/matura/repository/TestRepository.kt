package com.ahmedmatem.android.matura.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.TestRemoteDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.addUsername
import com.ahmedmatem.android.matura.network.models.addUuid
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class TestRepository {
    private val localDataSource: TestLocalDataSource by inject(TestLocalDataSource::class.java)
    private val remoteDataSource: TestRemoteDataSource by inject(TestRemoteDataSource::class.java)

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val username: String? by lazy { userPrefs.getUser()?.username }
    private val uuid: String by lazy { userPrefs.getUuid() }

    val testList: LiveData<List<Test>> by lazy {
        username?.let {
            localDataSource.getAllForUser(username)
        } ?: run { localDataSource.getAllForGuest(uuid) }
    }

    // Todo: (9/9) Create Account data source and use it here to receive User token

    /**
     * RefreshTestList - read all tests from remote server and
     * write them in local database. / Replace in case of insert conflict
     */
    suspend fun refreshTestList() {
        val testList = remoteDataSource.getTestList()
        testList?.let {
            username?.let {
                // Add username to each test in the list
                testList.addUsername(it)
            } ?: run {
                // Add Uuid to each test in the list
                testList.addUuid(uuid)
            }
            // Write tests in local database
            localDataSource.insert(*testList.toTypedArray())
        }
    }

    /**
     * RefreshLastTests - read last count number of tests from remote server
     * and write them in local database.
     */
    suspend fun refreshLastTests(count: Int = 1) {
        val testList = remoteDataSource.getLastTests(count)
        testList?.let {
            username?.let {
                // Add username to each test in the list
                testList.addUsername(it)
            } ?: run {
                // Add Uuid to each test in the list
                testList.addUuid(uuid)
            }
            // Write tests in local database
            localDataSource.insert(*testList.toTypedArray())
        }
    }
}