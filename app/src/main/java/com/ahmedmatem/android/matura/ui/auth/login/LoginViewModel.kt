package com.ahmedmatem.android.matura.ui.auth.login

import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class LoginViewModel(context: Context) : ViewModel() {

    val username = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    private val _isLoginButtonEnabled = MutableLiveData<Boolean>()
    val isLoginButtonEnabled: LiveData<Boolean> get() = _isLoginButtonEnabled

    fun validateLoginButtonEnableState() {
        _isLoginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    fun tryLoginWithUsernameAndPassword() {
        Log.d("Login", "tryLogin with username: ${username} and password: $password ")
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