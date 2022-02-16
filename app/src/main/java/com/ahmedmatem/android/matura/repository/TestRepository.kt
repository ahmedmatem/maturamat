package com.ahmedmatem.android.matura.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.datasource.local.TestLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.TestRemoteDataSource
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.addUsername
import com.ahmedmatem.android.matura.network.models.addUuid
import org.koin.java.KoinJavaComponent.inject

class TestRepository {
    private val localDataSource: TestLocalDataSource by inject(TestLocalDataSource::class.java)
    private val remoteDataSource: TestRemoteDataSource by inject(TestRemoteDataSource::class.java)

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val user: UserPrefs.User? by lazy { userPrefs.getUser() }
    private val uuid: String by lazy { userPrefs.getUuid() }

    val testList: LiveData<List<Test>> by lazy {
        user?.let {
            localDataSource.getAllForUser(it.username)
        } ?: run { localDataSource.getAllForGuest(uuid) }
    }

    /**
     * RefreshTestList - read all tests from remote server and
     * write them in local database. / Replace in case of insert conflict
     */
    suspend fun refreshTestList() {
        var testList: List<Test>? = null
        user?.let {
            // In case of User
            testList = remoteDataSource.getAllForUser(it.token!!)
            testList?.let { list ->
                list.addUsername(it.username)
            }
        } ?: run {
            // In case of Guest
            testList = remoteDataSource.getAllForGuest(uuid)
            testList?.let { list ->
                list.addUuid(uuid)
            }
        }

        testList?.let {
            localDataSource.insert(*it.toTypedArray())
        }
    }

    /**
     * RefreshLastTest
     */
    suspend fun refreshLastTest() {
        var testList: List<Test>? = null
        user?.let {
            // In case of User
            testList = remoteDataSource.getLastTestsForUser(it.token!!)
            testList?.let { list ->
                list.addUsername(it.username)
            }
        } ?: run {
            // In case of Guest
            testList = remoteDataSource.getLastTestsForGuest(uuid)
            testList?.let { list ->
                list.addUuid(uuid)
            }
        }

        testList?.let {
            localDataSource.insert(*it.toTypedArray())
        }
    }
}