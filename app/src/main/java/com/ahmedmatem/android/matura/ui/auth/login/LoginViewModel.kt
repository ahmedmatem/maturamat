package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Result.GenericError
import com.ahmedmatem.android.matura.network.Result.Success
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi
import com.ahmedmatem.android.matura.repository.AuthRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(context: Context) : BaseViewModel() {

    private val authRepository = AuthRepository(
        MaturaDb.getInstance(context).tokenDao,
        AuthApi.retrofitService
    )

    val username = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    private val _isLoginButtonEnabled = MutableLiveData<Boolean>()
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    fun validateLoginButtonEnableState() {
        _isLoginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    fun tryLoginWithUsernameAndPassword() {
        _isLoginButtonEnabled.value = false
        showLoading.value = true
        viewModelScope.launch {
            val response = authRepository.requestToken(username.value!!, password.value!!)
            when (response) {
                is Result.NetworkError -> onNetworkError()
                is GenericError -> onGenericError(response)
                is Success -> onSuccess(response.data)
            }
        }
    }

    private suspend fun onSuccess(data: Token) {
        authRepository.saveToken(data)
        showLoading.value = false
        _isLoginButtonEnabled.value = true
        _loginResult.value = LoginResult.SUCCESS
    }

    private fun onNetworkError() {
        TODO("Not yet implemented")
    }

    private fun onGenericError(response: GenericError) {
        TODO("Not yet implemented")
    }

    fun tryLoginWithGoogle() {

    }

    fun tryLoginWithFacebook() {

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