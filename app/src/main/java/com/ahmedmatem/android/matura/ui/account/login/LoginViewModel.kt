package com.ahmedmatem.android.matura.ui.account.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.bgDescription
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(val context: Context) : BaseViewModel() {

    private val _userPrefs: UserPrefs by lazy { UserPrefs(context) }

    private val _accountRepository by lazy {
        AccountRepository(
            MaturaDb.getInstance(context).accountDao,
            AccountApi.retrofitService
        )
    }

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    private val _loginButtonEnabled = MutableLiveData<Boolean>()
    val loginButtonEnabled: LiveData<Boolean>
        get() = _loginButtonEnabled

    private val _loginAttemptResult = MutableLiveData<Boolean>()
    val loginAttemptResult: LiveData<Boolean>
        get() = _loginAttemptResult

    private val _externalLoginFlow = MutableLiveData<ExternalLoginProvider>()
    val externalLoginFlow: LiveData<ExternalLoginProvider> = _externalLoginFlow

    fun validateLoginButtonEnableState() {
        _loginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    /**
     * This function request access token by user credentials in order to login.
     * If token is received as request response still we need to check if email address
     * is confirmed. If it is - login succeeded, otherwise not.
     * If token is not received, possible reasons are lack of Internet connection (onNetworkError) or
     * invalid user credentials(onGenericError - code 400, Bad Request).
     */
    fun loginWithLocalAccount() {
        _loginButtonEnabled.value = false
        showLoading.value = true
        viewModelScope.launch {
            when (val tokenResponse =
                _accountRepository.requestToken(username.value!!, password.value!!)) {
                is Result.Success -> {
                    // Request Email confirmation check
                    when (val emailResponse =
                        _accountRepository.hasEmailConfirmed(tokenResponse.data.userName)) {
                        is Result.Success -> {
                            if (emailResponse.data) {
                                // User exists and email has confirmed
                                onSuccess(tokenResponse.data)
                            } else {
                                // User exists but email is not confirmed yet
                                navigateToEmailConfirmation(tokenResponse.data.userName)
                            }
                        }
                        is Result.GenericError -> onGenericError(emailResponse)
                        is Result.NetworkError -> onNetworkError()
                    }
                }
                is Result.NetworkError -> onNetworkError()
                is Result.GenericError -> onGenericError(tokenResponse)
            }
            showLoading.value = false
        }
    }

    private fun loginWithLocalAccount(_username: String, _password: String) {
        username.value = _username
        password.value = _password
        loginWithLocalAccount()
    }

    fun navigateToRegistration() {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        )
    }

    fun loginWithGoogle() {
        _externalLoginFlow.value = ExternalLoginProvider.Google
    }

    fun loginWithFacebook() {
        TODO("Facebook login not yet implemented")
    }

    fun navigateToPasswordReset() {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToPasswordResetFragment()
        )
    }

    /**
     * Use this function to send idToken to the Server.
     * After idToken verified successfully on the server by external login Provider,
     * user will be able to request access token by its local account.
     */
    fun tokenSignIn(idToken: String, provider: String) {
        viewModelScope.launch {
            when (val result = _accountRepository.tokenSignIn(idToken, provider)) {
                is Result.Success -> onTokenSignInSuccess(result.data)
                is Result.GenericError -> {}
                is Result.NetworkError -> {}
            }
        }
    }

    /**
     * OnSuccess user email will be returned from the server. Use this email and
     * External secret key to request access token by login with local account credentials.
     * If email is null - new local account has just been created and new tokenSignIn
     * request is required in order to record user as external in the database.
     */
    private suspend fun onTokenSignInSuccess(data: ExternalLoginData) {
        if (data.needAccountConnection) {
            showToast.value = "Local account exist. Account connection required"
        } else {
            data.email?.let { email ->
                var password = BuildConfig.EXTERNAL_LOGIN_SECRET_KEY
                val user = _accountRepository.getUser(email)
                user?.let { user ->
                    password = user.password!!
                }
                loginWithLocalAccount(email, password)
            }
        }
    }

    private suspend fun onSuccess(token: Token) {
        _accountRepository.saveToken(token)
        _userPrefs.setUser(token.userName, password.value)
        _loginAttemptResult.value = true
    }

    private fun onNetworkError() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.bgDescription()
    }

    private fun navigateToEmailConfirmation(email: String) {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToEmailConfirmationFragment(email)
        )
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct LoginViewModel")
        }
    }
}