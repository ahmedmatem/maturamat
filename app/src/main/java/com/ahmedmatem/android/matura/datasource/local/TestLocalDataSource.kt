package com.ahmedmatem.android.matura.datasource.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.utils.providers.ContextProvider
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import org.koin.java.KoinJavaComponent.inject

class TestLocalDataSource {
    private val resourcesProvider: ResourcesProvider by inject(ResourcesProvider::class.java)

    private val _level: Int by lazy {
        resourcesProvider.getResources().getInteger(R.integer.level)
    }

    private val dataSource: TestDao by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.testDao
    }

    fun getAllForUser(username: String?): LiveData<List<Test>> {
        username?.let {
            return dataSource.getTestListByUsername(username, _level)
        } ?: run { return liveData { listOf<Test>() } }
    }

    fun getAllForGuest(uuid: String): LiveData<List<Test>> {
        return dataSource.getTestListByUuid(uuid, _level)
    }

    suspend fun insert(vararg tests: Test) {
        dataSource.insert(*tests)
    }

    suspend fun delete(vararg tests: Test) {
        dataSource.delete(*tests)
    }

    suspend fun isEmpty(username: String?): Boolean {
        username?.let {
            return !dataSource.isEmptyUserTestList(username)
        }
        return true
    }
}