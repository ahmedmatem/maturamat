package com.ahmedmatem.android.matura.ui.account.registration

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.infrastructure.PasswordOptions
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

    private val _fcmRegistrationTokenReceived = MutableLiveData(false)

    fun register() {
        if (isInputValid()) {
            hideValidationMessages()
            _showRegisterButton.value = false
            showLoading.value = true
            viewModelScope.launch {
                when (val response = _accountRepository.register(
                    username.value!!,
                    password.value!!,
                    passwordConfirm.value!!,
                    UserPrefs(context).getFcmToken()
                )) {
                    is Result.Success -> onSuccess()
                    is Result.GenericError -> onGenericError(response)
                    is Result.NetworkError -> onNetworkError()
                }
            }
        } else {
            val errors = inputValidator.errors
            invalidateUi(errors)
        }
    }

    private fun invalidateUi(errors: Error) {
        invalidateUsernameUi(errors)
        invalidatePasswordUi(errors)
        invalidatePasswordConfirmUi(errors)
    }

    private fun invalidateUsernameUi(errors: Error) {
        var showMessage = false
        if (errors.has(Error.EMAIL_REQUIRED)) {
            _usernameValidationMessage.value = context.getString(R.string.email_required_message)
            showMessage = true
        } else if (errors.has(Error.EMAIL_INVALID_FORMAT)) {
            _usernameValidationMessage.value = context.getString(R.string.email_invalid)
            showMessage = true
        }
        _showUsernameValidationMessage.value = showMessage
    }

    private fun invalidatePasswordUi(errors: Error) {
        if (errors.has(Error.PASSWORD_REQUIRED) ||
            errors.has(Error.PASSWORD_LOWERCASE_REQUIRED) ||
            errors.has(Error.PASSWORD_NON_ALPHANUMERIC_REQUIRED) ||
            errors.has(Error.PASSWORD_REQUIRED_LENGTH) ||
            errors.has(Error.PASSWORD_UPPERCASE_REQUIRED)
        ) {
            _passwordValidationMessage.value = context.getString(
                R.string.password_requirements_message,
                PasswordOptions.REQUIRED_LENGTH
            )
            _showPasswordValidationMessage.value = true
        }
    }

    private fun invalidatePasswordConfirmUi(errors: Error) {
        var showMessage = false
        if (errors.has(Error.PASSWORD_CONFIRM_REQUIRED)) {
            _passwordConfirmValidationMessage.value =
                context.getString(R.string.password_confirm_required_message)
            showMessage = true
        } else if (errors.has(Error.PASSWORDS_NO_MATCH)) {
            _passwordConfirmValidationMessage.value =
                context.getString(R.string.password_no_match_message)
            showMessage = true
        }
        _showPasswordConfirmValidationMessage.value = showMessage
    }

    private fun isInputValid(): Boolean {
        return inputValidator.isValid(
            username.value!!,
            password.value!!,
            passwordConfirm.value!!
        )
    }

    private fun hideValidationMessages() {
        _showUsernameValidationMessage.value = false
        _showPasswordValidationMessage.value = true
        _showPasswordConfirmValidationMessage.value = true
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