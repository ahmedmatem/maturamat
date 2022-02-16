package com.ahmedmatem.android.matura.datasource.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.network.models.Test
import org.koin.java.KoinJavaComponent.inject

class TestLocalDataSource {

    private val dataSource: TestDao by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.testDao
    }

    fun getAllForUser(username: String?): LiveData<List<Test>>? {
        username?.let {
            return dataSource.getTestListByUsername(username)
        } ?: run { return null }
    }

    fun getAllForGuest(uuid: String): LiveData<List<Test>> {
        return dataSource.getTestListByUsername(uuid)
    }

    suspend fun insert(vararg tests: Test) {
        dataSource.insert(*tests)
    }

    suspend fun delete(vararg tests: Test) {
        dataSource.delete(*tests)
    }
}