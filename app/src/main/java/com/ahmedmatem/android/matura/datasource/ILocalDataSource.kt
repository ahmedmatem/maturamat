package com.ahmedmatem.android.matura.datasource

import androidx.lifecycle.LiveData

interface ILocalDataSource<T> {
    fun getAll(id: String): LiveData<List<T>>
    suspend fun insert(vararg items: T)
    suspend fun delete(vararg items: T)
}