package com.ahmedmatem.android.matura.datasource.local

import com.ahmedmatem.android.matura.local.daos.Test2Dao
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class Test2LocalDataSource {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val dataSource: Test2Dao by inject(Test2Dao::class.java)

    suspend fun insert(vararg tests: Test2) {
        dataSource.insert(*tests)
    }
}