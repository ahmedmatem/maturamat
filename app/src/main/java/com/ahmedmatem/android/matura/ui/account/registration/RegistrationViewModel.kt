package com.ahmedmatem.android.matura.ui.account.registration

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RegistrationViewModel(private val context: Context) : BaseViewModel() {

    private val inputValidator by lazy { RegistrationInputValidator() }

    private val _accountRepository by lazy {
        AccountRepository(MaturaDb.getInstance(context).accountDao, AccountApi.retrofitService)
    }

    // Username input
    val username = MutableLiveData("")
    private val _usernameValidationMessage = MutableLiveData("")
    val usernameValidationMessage: LiveData<String> = _usernameValidationMessage
    private val _showUsernameValidationMessage = MutableLiveData(false)
    val showUsernameValidationMessage: LiveData<Boolean> = _showUsernameValidationMessage

    // Password input
    val password = MutableLiveData("")
    private val _passwordValidationMessage = MutableLiveData("")
    val passwordValidationMessage: LiveData<String> = _passwordValidationMessage
    private val _showPasswordValidationMessage = MutableLiveData(false)
    val showPasswordValidationMessage: LiveData<Boolean> = _showPasswordValidationMessage

    // PasswordConfirm input
    val passwordConfirm = MutableLiveData("")
    private val _passwordConfirmValidationMessage = MutableLiveData("")
    val passwordConfirmValidationMessage: LiveData<String> = _passwordConfirmValidationMessage
    private val _showPasswordConfirmValidationMessage = MutableLiveData(false)
    val showPasswordConfirmValidationMessage: LiveData<Boolean> =
        _showPasswordConfirmValidationMessage

    // button Register
    private val _showRegisterButton = MutableLiveData(true)
    val showRegisterButton: LiveData<Boolean> = _showRegisterButton

    init {
        // todo: request FCM registration token
    }

    fun register() {
        if (isInputValid()) {
            _showRegisterButton.value = false
            showLoading.value = true
            viewModelScope.launch {
                when (val response = _accountRepository.register(
                    username.value!!,
                    password.value!!,
                    passwordConfirm.value!!,
                    ""// todo: get FCM token
                )) {
                    is Result.Success -> onSuccess()
                    is Result.GenericError -> onGenericError(response)
                    is Result.NetworkError -> onNetworkError()
                }
            }
        } else {
            invalidateUi()
        }
    }

    private fun invalidateUi() {
        invalidateUsernameUi()
        invalidatePasswordui()
        invalidatePasswordConfirmUi()
    }

    private fun invalidateUsernameUi() {

    }

    private fun isInputValid(): Boolean {
        return inputValidator.isValid()
    }

    private fun onSuccess() {
        TODO("onSuccess for register is not implemented yet.")
    }

    private fun onGenericError(error: Result.GenericError) {
        TODO("onGenericError for register is not implemented yet.")
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand
            .To(RegistrationFragmentDirections.actionRegistrationFragmentToNoConnectionFragment())
    }

    class Factory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                return RegistrationViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct RegistrationViewModel")
        }

    }
}