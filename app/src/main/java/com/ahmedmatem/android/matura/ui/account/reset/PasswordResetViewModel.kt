package com.ahmedmatem.android.matura.ui.account.reset

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.network.HttpStatus
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.registration.Error
import com.ahmedmatem.android.matura.ui.account.registration.RegistrationFormValidator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class PasswordResetViewModel : BaseViewModel() {
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)

    private val _showSendButton = MutableLiveData(true)
    val showSendButton: LiveData<Boolean> = _showSendButton

    private val _showInvalidEmailMessage = MutableLiveData(false)
    val showInvalidEmailMessage: LiveData<Boolean> = _showInvalidEmailMessage
    private val _failMessage: MutableLiveData<String> = MutableLiveData()
    val failMessage: LiveData<String> = _failMessage
    private val _failCode: MutableLiveData<Int?> = MutableLiveData(null)
    val failCode: LiveData<Int?> = _failCode
    private val _emailError: MutableLiveData<Int?> = MutableLiveData(null)
    val emailError: LiveData<Int?> = _emailError

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
                _accountRepository.forgotPassword(email).collect { result ->
                    when(result) {
                        is Result.Success -> onSuccess()
                        is Result.GenericError -> onGenericError(result.code!!)
                        is Result.NetworkError -> onNetworkError()
                    }
                }
                showLoading.value = false
            }
        } else {
            val errors = inputValidator.errors
            showEmailAlert(errors)
        }
    }

    private fun onSuccess() {
        _showSuccessMessage.value = true
    }

    fun setSuccessMessage(message: String) {
        _successMessage.value = message
    }

    private fun onGenericError(responseCode: Int) {
        _showSendButton.value = true
        when (responseCode) {
            HttpStatus.NotFound.code -> {
                _failCode.value = responseCode
            }
            HttpStatus.BadRequest.code -> {
                _failCode.value = responseCode
            }
            else -> {}
        }
    }

    fun setFailMessage(message: String) {
        _failMessage.value = message
        _showInvalidEmailMessage.value = true
    }

    private fun onNetworkError() {
        _showSendButton.value = true
        navigationCommand.value = NavigationCommand.To(
            PasswordResetFragmentDirections.actionPasswordResetFragmentToNoConnectionFragment()
        )
    }

    private fun showEmailAlert(errors: Error) {
        if (errors.has(Error.EMAIL_REQUIRED)) {
            _emailError.value = Error.EMAIL_REQUIRED
        } else if (errors.has(Error.EMAIL_INVALID_FORMAT)) {
            _emailError.value = Error.EMAIL_INVALID_FORMAT
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PasswordResetViewModel::class.java)) {
                return PasswordResetViewModel() as T
            }
            throw IllegalArgumentException("Unable to create PasswordResetViewModel.")
        }
    }
}