package com.ahmedmatem.android.matura.repository

import com.ahmedmatem.android.matura.datasource.local.AccountLocalDataSource
import com.ahmedmatem.android.matura.datasource.remote.AccountRemoteDataSource
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class AccountRepository {
    private val remoteDataSource: AccountRemoteDataSource by inject(AccountRemoteDataSource::class.java)
    private val localDataSource: AccountLocalDataSource by inject(AccountLocalDataSource::class.java)

    /**
     * Local Requests
     */

    fun getUserLocal(username: String): Flow<User?> = localDataSource.userBy(username)

    fun isUserExistsLocal(username: String): Flow<Boolean> = localDataSource.isUserExists(username)

    fun save(user: User): Flow<Unit> = localDataSource.save(user)

    /**
     * Remote requests
     */

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

    fun isEmailConfirmed(email: String): Flow<Result<Boolean>> =
        remoteDataSource.isEmailConfirmed(email)

    fun sendEmailConfirmationLink(email: String): Flow<Result<Unit>> =
        remoteDataSource.sendEmailConfirmationLink(email)

    fun validateIdToken(token: String, provider: String): Flow<Result<ExternalLoginData>> =
        remoteDataSource.validateIdToken(token, provider)

    fun register(
        username: String,
        password: String,
        confirmPassword:
        String, fcmToken: String?
    ): Flow<Result<Unit>> = remoteDataSource.registration(username, password, confirmPassword, fcmToken)

    fun forgotPassword(email: String): Flow<Result<Unit>> = remoteDataSource.forgotPassword(email)

    fun updateFcmToken(email: String, fcmToken: String): Flow<Result<Unit>> =
        remoteDataSource.updateFcmToken(email, fcmToken)
}