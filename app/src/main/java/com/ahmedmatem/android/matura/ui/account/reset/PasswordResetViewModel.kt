package com.ahmedmatem.android.matura.ui.account.reset

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.HttpStatus
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.registration.Error
import com.ahmedmatem.android.matura.ui.account.registration.RegistrationFormValidator
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PasswordResetViewModel(private val context: Context) : BaseViewModel() {
    private val _accountRepository by lazy {
        AccountRepository(
            MaturaDb.getInstance(context).accountDao,
            AccountApi.retrofitService
        )
    }

    private val _showSendButton = MutableLiveData(true)
    val showSendButton: LiveData<Boolean> = _showSendButton

    private val _showInvalidEmailMessage = MutableLiveData(false)
    val showInvalidEmailMessage: LiveData<Boolean> = _showInvalidEmailMessage
    private val _failMessage: MutableLiveData<String> = MutableLiveData()
    val failMessage: LiveData<String> = _failMessage

    private val _successMessage = MutableLiveData("")
    val successMessage: LiveData<String> = _successMessage
    private val _showSuccessMessage = MutableLiveData(false)
    val showSuccessMessage: LiveData<Boolean> = _showSuccessMessage

    fun sendPasswordResetEmail(email: String) {
        val inputValidator = RegistrationFormValidator()
        if (inputValidator.isEmailValid(email)) {
            _showInvalidEmailMessage.value = false
            _showSendButton.value = false
            showLoading.value = true
            viewModelScope.launch {
                when (val response = _accountRepository.forgotPassword(email)) {
                    is Result.Success -> onSuccess(email)
                    is Result.GenericError -> onGenericError(response.code!!, email)
                    is Result.NetworkError -> onNetworkError()
                }
                showLoading.value = false
            }
        } else {
            val errors = inputValidator.errors
            showEmailAlert(errors)
        }
    }

    private fun onSuccess(email: String) {
        _successMessage.value = context.getString(R.string.forgot_password_success_message, email)
        _showSuccessMessage.value = true
    }

    private fun onGenericError(responseCode: Int, email: String) {
        _showSendButton.value = true
        when (responseCode) {
            HttpStatus.NotFound.code -> {
                _failMessage.value = context.getString(R.string.email_not_found, email)
                _showInvalidEmailMessage.value = true
            }
            HttpStatus.BadRequest.code -> {
                _failMessage.value = context.getString(R.string.email_required_message)
                _showInvalidEmailMessage.value = true
            }
            else -> {}
        }
    }

    private fun onNetworkError() {
        _showSendButton.value = true
        navigationCommand.value = NavigationCommand.To(
            PasswordResetFragmentDirections.actionPasswordResetFragmentToNoConnectionFragment()
        )
    }

    private fun showEmailAlert(errors: Error) {
        var showMessage = false
        if (errors.has(Error.EMAIL_REQUIRED)) {
            _failMessage.value = context.getString(R.string.email_required_message)
            showMessage = true
        } else if (errors.has(Error.EMAIL_INVALID_FORMAT)) {
            _failMessage.value = context.getString(R.string.email_invalid)
            showMessage = true
        }
        _showInvalidEmailMessage.value = showMessage
    }

    class Factory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PasswordResetViewModel::class.java)) {
                return PasswordResetViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to create PasswordResetViewModel.")
        }
    }
}