package com.ahmedmatem.android.matura.ui.account.reset

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.registration.Error
import com.ahmedmatem.android.matura.ui.account.registration.RegistrationInputValidator
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PasswordResetViewModel(private val context: Context) : BaseViewModel() {
    private val _accountRepository by lazy {
        AccountRepository(
            MaturaDb.getInstance(context).accountDao,
            AccountApi.retrofitService
        )
    }

    private val _showInvalidEmailMessage = MutableLiveData(false)
    val showInvalidPasswordMessage: LiveData<Boolean> = _showInvalidEmailMessage

    private val _failMessage: MutableLiveData<String> = MutableLiveData()
    val failMessage: LiveData<String> = _failMessage

    fun sendPasswordResetEmail(email: String) {
        val inputValidator = RegistrationInputValidator()
        if (inputValidator.isEmailValid(email)) {
            viewModelScope.launch {
                when (val response = _accountRepository.forgotPassword(email)) {
                    is Result.Success -> onSuccess()
                    is Result.GenericError -> onGenericError(response)
                    is Result.NetworkError -> onNetworkError()
                }
            }
        } else {
            val errors = inputValidator.errors
            showPasswordAlert(errors)
        }
    }

    private fun onSuccess() {

    }

    private fun onGenericError(response: Result<Unit>) {

    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand.To(
            PasswordResetFragmentDirections.actionPasswordResetFragmentToNoConnectionFragment()
        )
    }

    private fun showPasswordAlert(errors: Error) {
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
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PasswordResetViewModel::class.java)) {
                return PasswordResetViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to create PasswordResetViewModel.")
        }

    }
}