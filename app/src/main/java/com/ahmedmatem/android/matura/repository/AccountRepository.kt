package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.remote.AccountRemoteDataSource
import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class AccountRepository(
    // TODO: remove constructor parameters
    private val accountLocal: AccountDao,
    private val accountRemote: AccountApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val remoteDataSource: AccountRemoteDataSource by inject(AccountRemoteDataSource::class.java)

    /**
     * Local Requests
     */

    suspend fun getTokenLocal(username: String): String {
        return accountLocal.getToken(username)
    }

    suspend fun getUserLocal(username: String): User? {
        return accountLocal.getUser(username)
    }

    suspend fun userExistsLocal(username: String): Boolean {
        return accountLocal.isUserExists(username)
    }

    suspend fun saveUserLocal(user: User) {
        withContext(dispatcher) {
            accountLocal.insert(user)
        }
    }

    /**
     * Network requests
     */

    suspend fun requestToken(username: String, password: String): Result<User> {
        return safeApiCall(dispatcher) {
            accountRemote.requestToken(username, password)
        }
    }

    fun requestTokenFlow(
        username: String,
        password: String
    ): Flow<Result<User>> = remoteDataSource.requestToken(username, password)

    /**
     * Request Firebase Cloud Messaging token.
     * Wrapping callback listener with callbackFlow.
     */
    fun requestFcmToken(): Flow<String> = callbackFlow {
        val callback = OnCompleteListener<String> { task: Task<String> ->
            if (!task.isSuccessful) {
                // Log.w(TAG,"register: Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            trySendBlocking(token)
                .onFailure {
                    // Downstream has been cancelled or failed, can log here
                }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(callback)
        awaitClose {
            // Suspending function awaitClose() which takes a lambda to be invoked
            // when this channel is closed or canceled.
            // Terminate operation in callback properly here.
        }
    }

    suspend fun emailConfirmedRemote(email: String): Result<Boolean> {
        return safeApiCall(dispatcher) {
            accountRemote.isEmailConfirmed(email)
        }
    }

    fun isEmailConfirmed(email: String): Flow<Result<Boolean>> =
        remoteDataSource.isEmailConfirmed(email)

    suspend fun requestEmailConfirmationLink(email: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountRemote.sendEmailConfirmationLink(email)
        }
    }

    suspend fun validateIdTokenRemote(idToken: String, provider: String): Result<ExternalLoginData> {
        return safeApiCall(dispatcher) {
            accountRemote.validateIdToken(idToken, provider)
        }
    }

    fun registerFlow(
        username: String,
        password: String,
        confirmPassword:
        String, fcmToken: String?
    ): Flow<Result<Unit>> = remoteDataSource.registration(username, password, confirmPassword, fcmToken)

    suspend fun register(email: String, password: String, passwordConfirm: String, token: String?):
            Result<Unit> {
        return safeApiCall(dispatcher) {
            accountRemote.register(email, password, passwordConfirm, token)
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountRemote.forgotPassword(email)
        }
    }

    suspend fun sendFcmRegistrationToServer(email: String, token: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            accountRemote.sendFcmRegistrationToServer(email, token)
        }
    }
}