package com.ahmedmatem.android.matura.ui.account.registration

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.HttpStatus
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class RegistrationViewModel(args: RegistrationFragmentArgs) : BaseViewModel() {

    private val _inputValidator by lazy { RegistrationFormValidator() }
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)

    /**
     * Giving email as argument to the view model means
     * the registration will happen after external login attempt.
     **/
    private val _isRegistrationOnExternalLogin = args.email != null

    private val _regButtonUiState: MutableStateFlow<RegistrationButtonUiState> =
        MutableStateFlow(RegistrationButtonUiState())
    val regButtonUiState: StateFlow<RegistrationButtonUiState> = _regButtonUiState.asStateFlow()

    private val _regFormUiState: MutableStateFlow<RegistrationFormUiState> = MutableStateFlow(
        RegistrationFormUiState()
    )
    val regFormUiState: StateFlow<RegistrationFormUiState> = _regFormUiState.asStateFlow()

    /**
     * Use this property to prevent hiding of validation messages each time
     * after user registration form data changed.
     * Its initial value of false allow hiding of validation messages (if have such)
     * on registration form data changed.
     * Changing editText in registration form trigger hiding of all validation messages (if has such)
     * and set this property value of true. This will prevent hiding next time after text changed.
     * Reset its value to FALSE on each registration button click (in register function).
     **/
    private var _allValidationMessagesHidden: Boolean = false

    private var _username: String = args.email ?: ""
    private var _password: String = ""
    private var _confirmPassword: String = ""

    private val _onLoginComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    val onLoginComplete: LiveData<Boolean> = _onLoginComplete

    /**
     * Trigger on Registration button click
     */
    fun register() {
        /**
         * On register button click initializing this property to false will allow on next data change
         * to hide validation messages
         **/
        _allValidationMessagesHidden = false

        if (isInputValid()) {
            // Disable registration button
            _regButtonUiState.value = RegistrationButtonUiState(enable = false)
            showLoading.value = true

            viewModelScope.launch {
                if(!_isRegistrationOnExternalLogin) {
                    /**
                     * Requesting FCM token is a callbackFlow function and it is blocking the entire
                     * function from continuing, and it is awaiting for it to be closed.
                     * To get it going, we have to unblock it by putting it behind a launch.
                     */
                    launch {
                        _accountRepository.fcmToken().collect { fcmToken ->
                            runRegistration(fcmToken)
                        }
                    }
                } else {
                    runRegistration()
                }
            }
        } else {
            val errors = _inputValidator.errors
            invalidateUi(errors)
        }
    }

    /**
     * Parameter token should be null in case of registration caused by external login attempt.
     */
    private suspend fun runRegistration(token: String? = null) {
        _accountRepository.register(_username, _password, _confirmPassword, token)
            .collect { response ->
                // Enable registration button
                showLoading.value = false
                _regButtonUiState.value = RegistrationButtonUiState(enable = true)
                when(response) {
                    is Result.Success -> onRegistrationSuccess()
                    is Result.GenericError -> onGenericError(response)
                    is Result.NetworkError -> onNetworkError()
                }
            }
    }

    private fun invalidateUi(errors: Error) {
        val usernameValidationMessage = invalidateUsernameUi(errors)
        val passwordValidationMessage = invalidatePasswordUi(errors)
        val confirmPasswordValidationMessage = invalidateConfirmPasswordUi(errors)
        _regFormUiState.value = RegistrationFormUiState(
            showUsernameValidationMessage = usernameValidationMessage?.isNotBlank() ?: false,
            usernameValidationMessage = usernameValidationMessage ?: "",
            showPasswordValidationMessage = passwordValidationMessage?.isNotBlank() ?: false,
            passwordValidationMessage = passwordValidationMessage ?: "",
            showConfirmPasswordValidationMessage = confirmPasswordValidationMessage?.isNotBlank() ?: false,
            confirmPasswordValidationMessage = confirmPasswordValidationMessage ?: ""
        )
    }

    /**
     * Invalidate username. Return validation message. If it is null username is valid.
     */
    private fun invalidateUsernameUi(errors: Error): String? {
        var validationMessage: String? = null
        if (errors.has(Error.EMAIL_REQUIRED)) {
            validationMessage = Error.message[Error.EMAIL_REQUIRED]
        } else if (errors.has(Error.EMAIL_INVALID_FORMAT)) {
            validationMessage = Error.message[Error.EMAIL_INVALID_FORMAT]
        }
        return validationMessage
    }

    /**
     * Invalidate password. Return validation message. If it is null password is valid.
     */
    private fun invalidatePasswordUi(errors: Error): String? {
        return if (errors.has(Error.PASSWORD_REQUIRED) ||
            errors.has(Error.PASSWORD_LOWERCASE_REQUIRED) ||
            errors.has(Error.PASSWORD_NON_ALPHANUMERIC_REQUIRED) ||
            errors.has(Error.PASSWORD_REQUIRED_LENGTH) ||
            errors.has(Error.PASSWORD_UPPERCASE_REQUIRED)
        ) {
            Error.message[Error.PASSWORD_GENERAL_VALIDATION_MESSAGE]
        } else {
            null
        }
    }

    /**
     * Invalidate confirmPassword. Return validation message. If it is null confirmPassword is valid.
     */
    private fun invalidateConfirmPasswordUi(errors: Error): String? {
        var validationMessage: String? = null
        if (errors.has(Error.PASSWORD_CONFIRM_REQUIRED)) {
            validationMessage = Error.message[Error.PASSWORD_CONFIRM_REQUIRED]
        } else {
            if (errors.has(Error.PASSWORDS_NO_MATCH)) {
                validationMessage = Error.message[Error.PASSWORDS_NO_MATCH]
            }
        }
        return validationMessage
    }

    private fun isInputValid(): Boolean {
        return _inputValidator.isValid(_username, _password, _confirmPassword )
    }

    private fun hideValidationMessages() {
        _regFormUiState.value = RegistrationFormUiState() // hidden by default
        _allValidationMessagesHidden = true
    }

    private fun onRegistrationSuccess() {
        if (_isRegistrationOnExternalLogin) {
            loginWithLocalAccount()
        } else {
            navigationCommand.value = NavigationCommand.To(
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailConfirmationFragment(
                    _username
                )
            )
        }
    }

    private fun onGenericError(error: Result.GenericError) {
        if (error.code == HttpStatus.BadRequest.code) {
            _regFormUiState.value = RegistrationFormUiState(
                showFailMessage = true,
                failMessage = "Потребителското име е заето."
            )
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
    private fun loginWithLocalAccount() {
        viewModelScope.launch {
            _accountRepository.requestTokenFlow(_username, _password)
                .collect { tokenResponse ->
                    when(tokenResponse) {
                        is Result.Success -> saveUserCredentialsLocal(tokenResponse.data)
                        is Result.NetworkError -> onNetworkError()
                        is Result.GenericError -> onGenericError(tokenResponse)
                    }
                }
            showLoading.value = false
        }
    }

    private suspend fun saveUserCredentialsLocal(user: User) {
        _accountRepository.saveUserLocal(user)
        _userPrefs.setUser(user.username, user.password, user.token)
        _onLoginComplete.value = true
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val args: RegistrationFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                return RegistrationViewModel(args) as T
            }
            throw IllegalArgumentException("Unable to construct RegistrationViewModel")
        }

    }

    fun afterUsernameChanged(username: String) {
        _username = username
        if(!_allValidationMessagesHidden) {
            hideValidationMessages()
        }
        tryEnableRegisterButton()
    }

    fun afterPasswordChanged(password: String) {
        _password = password
        if(!_allValidationMessagesHidden) {
            hideValidationMessages()
        }
        tryEnableRegisterButton()
    }

    fun afterConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword = confirmPassword
        if(!_allValidationMessagesHidden) {
            hideValidationMessages()
        }
        tryEnableRegisterButton()
    }

    private fun tryEnableRegisterButton() {
        if(_regButtonUiState.value.enable) return
        if(_username.isNotBlank() && _password.isNotBlank() && _confirmPassword.isNotBlank()) {
            _regButtonUiState.value = RegistrationButtonUiState(enable = true)
        }
    }

    companion object {
        const val TAG: String = "RegistrationViewModel"
    }
}