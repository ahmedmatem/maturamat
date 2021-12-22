package com.ahmedmatem.android.matura.ui.account.registration

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.infrastructure.PasswordOptions
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.HttpStatus
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.models.withPassword
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class RegistrationViewModel(
    private val context: Context,
    private val args: RegistrationFragmentArgs
) : BaseViewModel() {

    private val _inputValidator by lazy { RegistrationInputValidator() }
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val _isExternalLogin = args.email != null

    // Username input
    val username = MutableLiveData(args.email ?: "")
    private val _usernameValidationMessage = MutableLiveData("")
    val usernameValidationMessage: LiveData<String> = _usernameValidationMessage
    private val _showUsernameValidationMessage = MutableLiveData(false)
    val showUsernameValidationMessage: LiveData<Boolean> = _showUsernameValidationMessage
    val usernameEnabled: Boolean = args.email == null

    // Password input
    val password = MutableLiveData("")
    private val _passwordValidationMessage = MutableLiveData("")
    val passwordValidationMessage: LiveData<String> = _passwordValidationMessage
    private val _showPasswordValidationMessage = MutableLiveData(false)
    val showPasswordValidationMessage: LiveData<Boolean> = _showPasswordValidationMessage

    // PasswordConfirm input
    val passwordConfirm = MutableLiveData("")
    private val _passwordConfirmValidationMessage = MutableLiveData("")
    val passwordConfirmValidationMessage: LiveData<String>
        get() = _passwordConfirmValidationMessage
    private val _showPasswordConfirmValidationMessage = MutableLiveData(false)
    val showPasswordConfirmValidationMessage: LiveData<Boolean>
        get() = _showPasswordConfirmValidationMessage

    // button Register
    private val _showRegisterButton = MutableLiveData(true)
    val showRegisterButton: LiveData<Boolean> = _showRegisterButton

    private val _failMessage = MutableLiveData("")
    val failMessage: LiveData<String> = _failMessage
    private val _showFailMessage = MutableLiveData(false)
    val showFailMessage: LiveData<Boolean> = _showFailMessage

    private val _fcmRegistrationTokenReceived = MutableLiveData(false)

    private val _onLoginComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    val onLoginComplete: LiveData<Boolean> = _onLoginComplete

    fun register() {
        if (isInputValid()) {
            hideValidationMessages()
            _showRegisterButton.value = false
            showLoading.value = true
            if (_isExternalLogin) {
                viewModelScope.launch {
                    when (val response = _accountRepository.register(
                        username.value!!,
                        password.value!!,
                        passwordConfirm.value!!,
                        null // null token means email is confirmed
                    )) {
                        is Result.Success -> onSuccess()
                        is Result.GenericError -> onGenericError(response)
                        is Result.NetworkError -> onNetworkError()
                    }
                    showLoading.value = false
                    _showRegisterButton.value = true
                }
            } else {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(
                            TAG,
                            "register: Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }
                    // Get new FCM registration token
                    val token = task.result
                    viewModelScope.launch {
                        when (val response = _accountRepository.register(
                            username.value!!,
                            password.value!!,
                            passwordConfirm.value!!,
                            token
                        )) {
                            is Result.Success -> onSuccess()
                            is Result.GenericError -> onGenericError(response)
                            is Result.NetworkError -> onNetworkError()
                        }
                        showLoading.value = false
                        _showRegisterButton.value = true
                    }
                })
            }
        } else {
            val errors = _inputValidator.errors
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
        return _inputValidator.isValid(
            username.value!!,
            password.value!!,
            passwordConfirm.value!!
        )
    }

    private fun hideValidationMessages() {
        _showUsernameValidationMessage.value = false
        _showPasswordValidationMessage.value = false
        _showPasswordConfirmValidationMessage.value = false
        _showFailMessage.value = false
    }

    private fun onSuccess() {
        if (_isExternalLogin) {
            loginWithLocalAccount()
        } else {
            navigationCommand.value = NavigationCommand.To(
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailConfirmationFragment(
                    username.value!!
                )
            )
        }
    }

    private fun onGenericError(error: Result.GenericError) {
        if (error.code == HttpStatus.BadRequest.code) {
            _failMessage.value = context.getString(R.string.email_taken_alert, username.value)
            _showFailMessage.value = true
        }
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand.To(
            RegistrationFragmentDirections.actionRegistrationFragmentToNoConnectionFragment()
        )
    }

    /**
     * This function request access token by user credentials in order to login.
     * If token is received as request response still we need to check if email address
     * is confirmed. If it is - login succeeded, otherwise not.
     * If token is not received, possible reasons are lack of Internet connection (onNetworkError) or
     * invalid user credentials(onGenericError - code 400, Bad Request).
     */
    fun loginWithLocalAccount() {
        showLoading.value = true
        viewModelScope.launch {
            // Request access token and ensure email is already confirmed before login
            when (val tokenResponse =
                _accountRepository.requestToken(username.value!!, password.value!!)) {
                is Result.Success -> login(tokenResponse.data.withPassword(password.value))
                is Result.NetworkError -> onNetworkError()
                is Result.GenericError -> onGenericError(tokenResponse)
            }
            showLoading.value = false
        }
    }

    private suspend fun login(user: User) {
        _accountRepository.saveUser(user)
        _userPrefs.setUser(user.userName, user.password)
        _onLoginComplete.value = true
    }

    class Factory(val context: Context, val args: RegistrationFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                return RegistrationViewModel(context, args) as T
            }
            throw IllegalArgumentException("Unable to construct RegistrationViewModel")
        }

    }

    companion object {
        const val TAG: String = "RegistrationViewModel"
    }
}