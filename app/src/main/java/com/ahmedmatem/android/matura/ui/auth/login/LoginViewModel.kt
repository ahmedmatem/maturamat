package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Result.GenericError
import com.ahmedmatem.android.matura.network.Result.Success
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.auth.login.external.ExternalLoginProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

    private val _isLoginButtonEnabled = MutableLiveData<Boolean>()
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    private val _loginAttemptResult = MutableLiveData<LoginResult>()
    val loginAttemptResult: LiveData<LoginResult> get() = _loginAttemptResult

    fun validateLoginButtonEnableState() {
        _isLoginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    fun loginWithLocalAccount() {
        _isLoginButtonEnabled.value = false
        showLoading.value = true
        viewModelScope.launch {
            when (val response =
                _accountRepository.requestToken(username.value!!, password.value!!)) {
                is Result.NetworkError -> onNetworkError()
                is GenericError -> onGenericError(response)
                is Success -> onSuccess(response.data)
            }
        }
    }

    fun navigateToRegistration() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
    }

    fun loginWithGoogle() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToGoogleLoginFragment())
    }

    fun loginWithFacebook() {
        TODO("Not yet implemented")
    }

    private suspend fun onSuccess(data: Token) {
        _accountRepository.saveToken(data)
        _prefs.setUser(data.userName)
        showLoading.value = false
        _loginAttemptResult.value = LoginResult.SUCCESS
    }

    private fun onNetworkError() {
        TODO("Not yet implemented")
    }

    private fun onGenericError(response: GenericError) {
        TODO("Not yet implemented")
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