package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.safeApiCall
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(
    private val accountDao: AccountDao,
    private val accountService: AccountApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun requestToken(username: String, password: String): Result<Token> {
        return safeApiCall(dispatcher) {
            accountService.getToken(username, password)
        }
    }

    suspend fun saveToken(token: Token) {
        withContext(dispatcher) {
            accountDao.insert(token)
        }
    }

    suspend fun hasEmailConfirmed(email: String): Result<Boolean> {
        return safeApiCall(dispatcher) {
            accountService.hasEmailConfirmed(email)
        }
    }

    suspend fun sendEmailConfirmationLink(email: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountService.sendEmailConfirmationLink(email)
        }
    }

    fun sendFcmRegistrationToServer(email: String, token: String) {
        accountService.sendFcmRegistrationToServer(email, token)
    }
}