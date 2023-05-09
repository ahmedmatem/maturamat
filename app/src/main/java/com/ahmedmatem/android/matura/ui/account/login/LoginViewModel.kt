package com.ahmedmatem.android.matura.ui.account.login

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.infrastructure.PasswordOptions
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.descriptionBg
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.models.withPassword
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import com.ahmedmatem.android.matura.ui.account.login.external.LoginAccompanyingAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel() : BaseViewModel() {

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)

    private val _loginFormUiState: MutableStateFlow<LoginFormUiState> = MutableStateFlow(LoginFormUiState())
    val loginFormUiState: StateFlow<LoginFormUiState> = _loginFormUiState.asStateFlow()

    private val _loginSuccessState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginSuccessState: StateFlow<Boolean> = _loginSuccessState

    private var _isExternalLogin: Boolean = false
    private var _username: String = ""
    private var _password: String = ""

    private val _externalLoginFlow = MutableLiveData<ExternalLoginProvider?>()
    val externalLoginFlow: LiveData<ExternalLoginProvider?>
        get() = _externalLoginFlow

    /**
     * This function request access token by user credentials in order to login.
     * If token is received as request response still we need to check if email address
     * is confirmed. If it is - login succeeded, otherwise not.
     * If token is not received, possible reasons are lack of Internet connection (onNetworkError) or
     * invalid user credentials(onGenericError - code 400, Bad Request).
     */
    fun login() {
        if(!isValidUser()) return
        viewModelScope.launch {
            showLoading.value = true
            _accountRepository.requestTokenFlow(_username, _password)
                .collect { tokenResponse ->
                    when(tokenResponse) {
                        is Result.Success -> onLoginSuccess(tokenResponse.data)
                        is Result.NetworkError -> onNetworkError()
                        is Result.GenericError -> onGenericError(tokenResponse)
                    }
                }
            showLoading.value = false
        }
    }

    fun navigateToRegistration() {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToRegistrationFragment(null)
        )
    }

    fun loginWithGoogle() {
        _externalLoginFlow.value = ExternalLoginProvider.Google
    }

    fun onExternalLoginComplete() {
        _externalLoginFlow.value = null
    }

    fun navigateToPasswordReset() {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToPasswordResetFragment()
        )
    }

    /**
     * Use this function to send idToken to the Server.
     * After idToken verified successfully on the server by external login Provider,
     * user will be able to request access token by its local account.
     */
    fun validateIdToken(idToken: String, provider: String) {
        viewModelScope.launch {
            when (val result = _accountRepository.validateIdTokenRemote(idToken, provider)) {
                is Result.Success -> onValidIdToken(result.data)
                is Result.GenericError -> onGenericError(result)
                is Result.NetworkError -> onNetworkError()
            }
        }
    }

    private suspend fun onValidIdToken(data: ExternalLoginData) {
        when (data.accompanyingAction) {
            LoginAccompanyingAction.Login -> externalLogin(data)
            LoginAccompanyingAction.CreateAccount -> {
                navigationCommand.value = NavigationCommand.To(
                    LoginFragmentDirections.actionLoginFragmentToRegistrationFragment(data.email)
                )
            }
        }
    }

    private suspend fun externalLogin(data: ExternalLoginData) {
        val userLocal = _accountRepository.getUserLocal(data.email!!)
        if (userLocal != null) {
            // Save username and password in viewModel for further use in login process
            _username = userLocal.username
            _password = userLocal.password!!
            // Set login by external provider
            _isExternalLogin = true
            login()
        } else {
            navigationCommand.value = NavigationCommand.To(
                LoginFragmentDirections.actionLoginFragmentToConfirmAccountFragment(
                    data.email!!, data.loginProvider
                )
            )
        }
    }

    private suspend fun saveUserCredentialsLocal(user: User) {
        Log.d("DEBUG2", "saveUserCredentialsLocal: after success login")
        _accountRepository.saveUserLocal(user)
        _userPrefs.setUser(user.username, user.password, user.token)
        _loginSuccessState.value = true
    }

    private suspend fun onLoginSuccess(user: User) {
        if(_isExternalLogin) {
            _isExternalLogin = false
            /**
             * In case of external login (email is always confirmed)
             */
            saveUserCredentialsLocal(user.withPassword(_password))
        } else {
            /**
             *  In case of login with local account
             *
             *  Check user if exists in local database.
             *  It exists there only if his email has already confirmed.
             *  But user no existing in local database does not guarantee that his
             *  email has not confirmed - i.e in case of user try to login from another
             *  device.
             */
            val hasEmailConfirmed = _accountRepository.userExistsLocal(_username)
            if (hasEmailConfirmed) {
                saveUserCredentialsLocal(user.withPassword(_password))
            } else {
                /**
                 * Try to check email confirmation in the server
                 */
                _accountRepository.isEmailConfirmed(user.username)
                    .collect { emailConfirmedResult ->
                        when(emailConfirmedResult) {
                            is Result.Success -> {
                                if (emailConfirmedResult.data) {
                                    // User exists and email has confirmed
                                    saveUserCredentialsLocal(user.withPassword(_password))
                                } else {
                                    // User exists but email is not confirmed yet
                                    navigateToEmailConfirmation(user.username)
                                }
                            }
                            is Result.GenericError -> onGenericError(emailConfirmedResult)
                            is Result.NetworkError -> onNetworkError()
                        }
                    }
            }
        }
    }

    private fun onNetworkError() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: Result.GenericError) {
        showToast.value = response.errorResponse?.descriptionBg()
    }

    private fun navigateToEmailConfirmation(email: String) {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToEmailConfirmationFragment(email)
        )
    }

    fun afterUsernameChanged(username: String) {
        _username = username
        _loginFormUiState.value = LoginFormUiState(
            enableLoginButton = _username.isNotBlank() && _password.isNotBlank()
        )
    }

    fun afterPasswordChanged(password: String) {
        _password = password
        _loginFormUiState.value = LoginFormUiState(
            enableLoginButton = _username.isNotBlank() && _password.isNotBlank()
        )
    }

    /**
     * This function triggered if only login button is clicked
     */
    private fun isValidUser() : Boolean {
        val isValid = isEmailValid(_username) && isPasswordValid(_password)
        if(!isValid) {
            // Show proper validation messages
            val currentUiState = _loginFormUiState.value
            _loginFormUiState.value = LoginFormUiState(
                enableLoginButton = currentUiState.enableLoginButton,
                showUsernameValidationMessage = !isEmailValid(_username),
                showPasswordValidationMessage = !isPasswordValid(_password)
            )
        }
        return isValid
    }

    private fun isEmailValid(email: String): Boolean {
        return if(TextUtils.isEmpty(email)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun isPasswordValid(pass: String): Boolean {
        // Validate password required length
        return pass.length >= PasswordOptions.REQUIRED_LENGTH
    }
}