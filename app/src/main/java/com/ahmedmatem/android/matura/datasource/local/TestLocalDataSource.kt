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

    fun getAll(usernameOrUuid: String): LiveData<List<Test>> {
        Log.d("DEBUG", "$usernameOrUuid: ")
        return dataSource.getAllBy(usernameOrUuid)
    }

    suspend fun insert(vararg tests: Test) {
        dataSource.insert(*tests)
    }

    suspend fun delete(vararg tests: Test) {
        dataSource.delete(*tests)
    }
}