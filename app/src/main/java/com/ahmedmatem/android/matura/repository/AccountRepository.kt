package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.local.AccountLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.AccountRemoteDataSource
import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class AccountRepository(
    // TODO: remove constructor parameters
    private val accountLocal: AccountDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val remoteDataSource: AccountRemoteDataSource by inject(AccountRemoteDataSource::class.java)
    private val localDataSource: AccountLocalDataSource by inject(AccountLocalDataSource::class.java)

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
     * Remote requests
     */

//    suspend fun requestToken(username: String, password: String): Result<User> {
//        return safeApiCall(dispatcher) {
//            accountRemote.requestToken(username, password)
//        }
//    }

    /**
     * Request access token for user - grant type is password.
     */
    fun token(
        username: String,
        password: String
    ): Flow<Result<User>> = remoteDataSource.token(username, password)

    /**
     * Request Firebase Cloud Messaging token.
     * Wrapping callback listener with callbackFlow.
     *
     * Requesting FCM token is a callbackFlow function and it is blocking the entire
     * function from continuing, and it is awaiting for it to be closed.
     * To get it going, we have to unblock it by putting it behind a launch.
     */
    fun fcmToken(): Flow<String> = remoteDataSource.fcmToken

//    suspend fun emailConfirmedRemote(email: String): Result<Boolean> {
//        return safeApiCall(dispatcher) {
//            accountRemote.isEmailConfirmed(email)
//        }
//    }

    fun isEmailConfirmed(email: String): Flow<Result<Boolean>> =
        remoteDataSource.isEmailConfirmed(email)

//    suspend fun requestEmailConfirmationLink(email: String): Result<Unit> {
//        return safeApiCall(dispatcher) {
//            accountRemote.sendEmailConfirmationLink(email)
//        }
//    }

    fun sendEmailConfirmationLink(email: String): Flow<Result<Unit>> =
        remoteDataSource.sendEmailConfirmationLink(email)

//    suspend fun validateIdTokenRemote(idToken: String, provider: String): Result<ExternalLoginData> {
//        return safeApiCall(dispatcher) {
//            accountRemote.validateIdToken(idToken, provider)
//        }
//    }

    fun validateIdToken(token: String, provider: String): Flow<Result<ExternalLoginData>> =
        remoteDataSource.validateIdToken(token, provider)

    fun register(
        username: String,
        password: String,
        confirmPassword:
        String, fcmToken: String?
    ): Flow<Result<Unit>> = remoteDataSource.registration(username, password, confirmPassword, fcmToken)

//    suspend fun register(email: String, password: String, passwordConfirm: String, token: String?):
//            Result<Unit> {
//        return safeApiCall(dispatcher) {
//            accountRemote.register(email, password, passwordConfirm, token)
//        }
//    }

//    suspend fun forgotPassword(email: String): Result<Unit> {
//        return safeApiCall(dispatcher) {
//            accountRemote.forgotPassword(email)
//        }
//    }

    fun forgotPassword(email: String): Flow<Result<Unit>> = remoteDataSource.forgotPassword(email)

//    suspend fun sendFcmRegistrationToServer(email: String, token: String): Result<Unit> {
//        return safeApiCall(dispatcher) {
//            accountRemote.updateFcmRegistrationToken(email, token)
//        }
//    }


    fun updateFcmToken(email: String, fcmToken: String): Flow<Result<Unit>> =
        remoteDataSource.updateFcmToken(email, fcmToken)
}