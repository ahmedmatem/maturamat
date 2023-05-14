package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class AccountRemoteDataSource {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val accountApiService: AccountApiService = AccountApi.retrofitService

    fun registration(
        username: String,
        password: String,
        confirmPassword: String,
        fcmToken: String?): Flow<Result<Unit>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.register(username, password, confirmPassword, fcmToken)
        }
        emit(result)
    }

    /**
     * User access token
     */
    fun token(username: String, password: String): Flow<Result<User>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.requestToken(username, password)
        }
        emit(result)
    }

    /**
     * Request Firebase Cloud Messaging token.
     * Wrapping callback listener with callbackFlow.
     */
    val fcmToken: Flow<String> = callbackFlow<String> {
        val callback = OnCompleteListener<String> {task ->
            if(!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            trySendBlocking(token)
                .onFailure {
                    // Downstream has been cancelled or failed, can log here
                }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(callback)
        awaitClose {
            /**
             * Suspending function awaitClose() which takes a lambda to be invoked when this
             * channel is closed or canceled. Terminate operation in callback properly here.
             */
        }
    }

    /**
     * Update FCM registration token in the server.
     */
    fun updateFcmToken(email: String, fcmToken: String): Flow<Result<Unit>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.updateFcmRegistrationToken(email, fcmToken)
        }
        emit(result)
    }

    fun forgotPassword(email: String): Flow<Result<Unit>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.forgotPassword(email)
        }
        emit(result)
    }

    fun isEmailConfirmed(email: String): Flow<Result<Boolean>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.isEmailConfirmed(email)
        }
        emit(result)
    }

    fun sendEmailConfirmationLink(email: String): Flow<Result<Unit>> = flow {
        val result = safeApiCall(dispatcher) {
            accountApiService.sendEmailConfirmationLink(email)
        }
        emit(result)
    }
}