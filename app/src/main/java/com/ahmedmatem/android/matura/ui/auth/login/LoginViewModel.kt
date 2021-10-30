package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result.NetworkError
import com.ahmedmatem.android.matura.network.Result.GenericError
import com.ahmedmatem.android.matura.network.Result.Success
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi
import com.ahmedmatem.android.matura.network.bgDescription
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(val context: Context) : BaseViewModel() {

    private val _prefs: UserPrefs by lazy { UserPrefs(context) }

    private val _accountRepository by lazy {
        AccountRepository(
            MaturaDb.getInstance(context).tokenDao,
            AuthApi.retrofitService
        )
    }

    val username = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    private val _loginButtonEnabled = MutableLiveData<Boolean>()
    val loginButtonEnabled: LiveData<Boolean> get() = _loginButtonEnabled

    private val _loginAttemptResult = MutableLiveData<LoginResult>()
    val loginAttemptResult: LiveData<LoginResult> get() = _loginAttemptResult

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
            when (val result =
                _accountRepository.requestToken(username.value!!, password.value!!)) {
                is Success -> {
                    showSnackBarInt.value = R.string.email_confirmation_in_progress_message
                    checkEmailConfirmation(result.data)
                }
                is NetworkError -> onNetworkError()
                is GenericError -> onGenericError(result)
            }
        }
    }

    fun navigateToRegistration() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
    }

    fun loginWithGoogle() {
        TODO("Google login not yet implemented")
//        navigationCommand.value =
//            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToGoogleLoginFragment())
    }

    fun loginWithFacebook() {
        TODO("Facebook login not yet implemented")
    }

    private suspend fun checkEmailConfirmation(token: Token) {
        when (val response = _accountRepository.emailConfirmed(token.userName)) {
            is Success -> {
                if (response.data) {
                    // Email address is confirmed.
                    _accountRepository.saveToken(token)
                    _prefs.setUser(token.userName)
                    _loginAttemptResult.value = LoginResult.SUCCESS
                } else {
                    // Email address is not confirmed
                    _loginAttemptResult.value = LoginResult.EMAIL_CONFIRMATION_REQUIRED
                }
            }
            else -> onNetworkError()
        }
    }

    private fun onNetworkError() {
        showLoading.value = false
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: GenericError) {
        showLoading.value = false
        showSnackBar.value = response.errorResponse?.bgDescription()
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