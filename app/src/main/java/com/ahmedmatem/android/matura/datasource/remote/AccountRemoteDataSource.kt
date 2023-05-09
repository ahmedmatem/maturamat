package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRemoteDataSource(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val accountApiService: AccountApiService = AccountApi.retrofitService

    fun requestToken(username: String, password: String): Flow<Result<User>> = flow {
        val tokenResponse = safeApiCall(dispatcher) {
            accountApiService.requestToken(username, password)
        }
        emit(tokenResponse)
    }

    fun isEmailConfirmed(email: String): Flow<Result<Boolean>> = flow {
        val emailConfirmedResponse = safeApiCall(dispatcher) {
            accountApiService.isEmailConfirmed(email)
        }
        emit(emailConfirmedResponse)
    }
}