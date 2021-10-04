package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Result.GenericError
import com.ahmedmatem.android.matura.network.Result.Success
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi
import com.ahmedmatem.android.matura.repository.AuthRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(context: Context) : ViewModel() {

    private val authRepository = AuthRepository(AuthApi.retrofitService)

    val username = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    private val _isLoginButtonEnabled = MutableLiveData<Boolean>()
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    fun validateLoginButtonEnableState() {
        _isLoginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    fun tryLoginWithUsernameAndPassword() {
        viewModelScope.launch {
            val response = authRepository.requestToken(username.value!!, password.value!!)
            when (response) {
                is Result.NetworkError -> showNetworkError()
                is GenericError -> showGenericError(response)
                is Success -> {

                }
            }
        }
    }

    private fun showNetworkError() {
        TODO("Not yet implemented")
    }

    private fun showGenericError(response: GenericError) {
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