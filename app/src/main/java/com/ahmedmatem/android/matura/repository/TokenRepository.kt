package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.safeApiCall
import com.ahmedmatem.android.matura.network.services.AuthApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TokenRepository(
    private val authService: AuthApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun requestToken(username: String, password: String): Result<Token> {
        return safeApiCall(dispatcher) {
            authService.getToken(username, password)
        }
    }
}