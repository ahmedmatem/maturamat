package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.AccountApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

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

    suspend fun tokenSignIn(idToken: String, provider: String): Result<String?> {
        return safeApiCall(dispatcher) {
            accountService.tokenSignIn(idToken, provider)
        }
    }

    suspend fun register(email: String, password: String, passwordConfirm: String, token: String):
            Result<Unit> {
        return safeApiCall(dispatcher) {
            accountService.register(email, password, passwordConfirm, token)
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountService.forgotPassword(email)
        }
    }

    suspend fun sendFcmRegistrationToServer(email: String, token: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountService.sendFcmRegistrationToServer(email, token)
        }
    }
}