package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(
    private val accountDao: AccountDao,
    private val accountService: AccountApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Request token from the Server
     */
    suspend fun requestToken(username: String, password: String): Result<Token> {
        return safeApiCall(dispatcher) {
            accountService.getToken(username, password)
        }
    }

    /**
     * Read token for user from database.
     * On App start it wil be checked for expiration and if it is
     * request for new token will be triggered.
     */
    suspend fun getToken(username: String): String {
        return accountDao.getToken(username)
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

    suspend fun validateIdToken(idToken: String, provider: String): Result<ExternalLoginData> {
        return safeApiCall(dispatcher) {
            accountService.validateIdToken(idToken, provider)
        }
    }

    suspend fun getUser(username: String): Token? {
        return accountDao.getUser(username)
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