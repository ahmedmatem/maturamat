package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Result.GenericError
import com.ahmedmatem.android.matura.network.Result.Success
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi
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

    private val _isLoginButtonEnabled = MutableLiveData<Boolean>()
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    private val _loginAttemptResult = MutableLiveData<LoginResult>()
    val loginAttemptResult: LiveData<LoginResult> get() = _loginAttemptResult

    fun validateLoginButtonEnableState() {
        _isLoginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    fun tryLoginWithUsernameAndPassword() {
        _isLoginButtonEnabled.value = false
        showLoading.value = true
        viewModelScope.launch {
            val response = _accountRepository.requestToken(username.value!!, password.value!!)
            when (response) {
                is Result.NetworkError -> onNetworkError()
                is GenericError -> onGenericError(response)
                is Success -> onSuccess(response.data)
            }
        }
    }

    private suspend fun onSuccess(data: Token) {
        // save token in database
        _accountRepository.saveToken(data)
        // set user preference
        _prefs.setUser(data.userName)
        // hide loading
        showLoading.value = false
        // Set login attempt result SUCCESS.
        // It will trigger navigate back to account tab in MainActivity
        _loginAttemptResult.value = LoginResult.SUCCESS
    }

    private fun onNetworkError() {
        TODO("Not yet implemented")
    }

    private fun onGenericError(response: GenericError) {
        TODO("Not yet implemented")
    }

    fun tryLoginWithGoogle() {
        TODO("Not yet implemented")
    }

    fun tryLoginWithFacebook() {
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