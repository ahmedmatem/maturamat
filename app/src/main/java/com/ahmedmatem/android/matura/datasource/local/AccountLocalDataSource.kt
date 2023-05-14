package com.ahmedmatem.android.matura.datasource.local

import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

class AccountLocalDataSource {
    private val _accountDao: AccountDao by inject(AccountDao::class.java)

    fun userBy(username: String): Flow<User?> = flow {
        val user = _accountDao.getUser(username)
        emit(user)
    }

    fun isUserExists(username: String): Flow<Boolean> = flow {
        val isExists = _accountDao.isUserExists(username)
        emit(isExists)
    }

    fun save(user: User): Flow<Unit> = flow {
        _accountDao.insert(user)
    }
}