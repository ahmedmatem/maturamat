package com.ahmedmatem.android.matura.datasource.local

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.datasource.ILocalDataSource
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.network.models.Test
import org.koin.java.KoinJavaComponent.inject

class TestLocalDataSource: ILocalDataSource<Test> {

    private val dataSource: TestDao by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.testDao
    }

    override fun getAll(usernameOrUuid: String): LiveData<List<Test>> {
        return dataSource.getAllBy(usernameOrUuid)
    }

    override suspend fun insert(vararg tests: Test) {
        dataSource.insert(*tests)
    }

    override suspend fun delete(vararg tests: Test) {
        dataSource.delete(*tests)
    }
}