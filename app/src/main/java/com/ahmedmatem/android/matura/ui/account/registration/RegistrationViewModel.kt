package com.ahmedmatem.android.matura.ui.account.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import kotlinx.coroutines.launch

class RegistrationViewModel : BaseViewModel() {

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
    val showPasswordConfirmValidationMessage: LiveData<Boolean> = _showPasswordConfirmValidationMessage

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

            }
        }
    }

    private fun isInputValid(): Boolean {
        return RegistrationInputValidator(
            username.value!!,
            password.value!!,
            passwordConfirm.value!!
        ).isValid()
    }
}