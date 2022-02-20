package com.ahmedmatem.android.matura.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
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
    private val uuid: String by lazy { userPrefs.getUuid() }

    /**
     * Refresh test list in local db with list from remote db.
     */
    suspend fun refreshTestList() {
        var testList: List<Test>? = null
        val user = userPrefs.getUser()
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
     * Refresh last test in local db with test from remote db.
     */
    suspend fun refreshLastTest() {
        var testList: List<Test>? = null
        val user = userPrefs.getUser()
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

    /**
     * Get Test list from local db
     */
    fun getTestList(): LiveData<List<Test>> {
        val user = userPrefs.getUser()
        return if (user != null) {
            localDataSource.getAllForUser(user?.username!!)
        } else {
            localDataSource.getAllForGuest(uuid)
        }
    }

    suspend fun refreshTestById(testId: String) {
        val test = remoteDataSource.getTestById(testId)
        test?.let {
            userPrefs.getUser()?.let { user ->
                test.addUsername(user.username)
            } ?: /*guest*/run {
                test.addUuid(uuid)
            }
            // populate db
            localDataSource.insert(test)
        }
    }
}