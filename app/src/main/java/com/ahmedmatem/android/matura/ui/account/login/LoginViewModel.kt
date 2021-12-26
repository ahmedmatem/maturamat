package com.ahmedmatem.android.matura.ui.account.login

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.bgDescription
import com.ahmedmatem.android.matura.network.models.withPassword
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
import com.ahmedmatem.android.matura.ui.account.login.external.LoginAccompanyingAction
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class LoginViewModel(val context: Context) : BaseViewModel() {

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    private var externalLogin = false

    private val _loginButtonEnabled = MutableLiveData<Boolean>()
    val loginButtonEnabled: LiveData<Boolean>
        get() = _loginButtonEnabled

    private val _loginAttemptResult = MutableLiveData<Boolean>()
    val loginAttemptResult: LiveData<Boolean>
        get() = _loginAttemptResult

    private val _externalLoginFlow = MutableLiveData<ExternalLoginProvider?>()
    val externalLoginFlow: LiveData<ExternalLoginProvider?> = _externalLoginFlow

    fun validateLoginButtonEnableState() {
        _loginButtonEnabled.value = username.value!!.isNotBlank() && password.value!!.isNotBlank()
    }

    /**
     * This function request access token by user credentials in order to login.
     * If token is received as request response still we need to check if email address
     * is confirmed. If it is - login succeeded, otherwise not.
     * If token is not received, possible reasons are lack of Internet connection (onNetworkError) or
     * invalid user credentials(onGenericError - code 400, Bad Request).
     */
    fun loginWithLocalAccount() {
        _loginButtonEnabled.value = false
        showLoading.value = true
        viewModelScope.launch {
            // Request access token and ensure email is already confirmed before login
            when (val tokenResponse =
                _accountRepository.requestToken(username.value!!, password.value!!)) {
                is Result.Success -> {
                    if (externalLogin) {
                        /**
                         * In case of external login (email is always confirmed)
                         */
                        login(tokenResponse.data.withPassword(password.value))
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
                        val hasEmailConfirmed = _accountRepository.isUserExist(username.value!!)
                        if (hasEmailConfirmed) {
                            login(tokenResponse.data.withPassword(password.value!!))
                        } else {
                            /**
                             * Try to check email confirmation in the server
                             */
                            when (val emailResponse =
                                _accountRepository.hasEmailConfirmed(tokenResponse.data.userName)) {
                                is Result.Success -> {
                                    if (emailResponse.data) {
                                        // User exists and email has confirmed
                                        login(tokenResponse.data.withPassword(password.value))
                                    } else {
                                        // User exists but email is not confirmed yet
                                        navigateToEmailConfirmation(tokenResponse.data.userName)
                                    }
                                }
                                is Result.GenericError -> onGenericError(emailResponse)
                                is Result.NetworkError -> onNetworkError()
                            }
                        }
                    }
                }
                is Result.NetworkError -> onNetworkError()
                is Result.GenericError -> onGenericError(tokenResponse)
            }
            showLoading.value = false
        }
    }

    private fun externalLogin(username: String, password: String) {
        this.username.value = username
        this.password.value = password
        externalLogin = true
        loginWithLocalAccount()
    }

    fun navigateToRegistration() {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToRegistrationFragment(null)
        )
    }

    fun loginWithGoogle() {
        _externalLoginFlow.value = ExternalLoginProvider.Google
    }

    fun loginWithFacebook() {
        _externalLoginFlow.value = ExternalLoginProvider.Facebook
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
            when (val result = _accountRepository.validateIdToken(idToken, provider)) {
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
        val user = _accountRepository.getUser(data.email!!)
        if (user?.password != null) {
            externalLogin(user.userName, user.password!!)
        } else {
            navigationCommand.value = NavigationCommand.To(
                LoginFragmentDirections.actionLoginFragmentToConfirmAccountFragment(
                    data.email!!, data.loginProvider
                )
            )
        }
    }

    private suspend fun login(user: User) {
        _accountRepository.saveUser(user)
        _userPrefs.setUser(user.userName, user.password)
        _loginAttemptResult.value = true
    }

    private fun onNetworkError() {
        navigationCommand.value =
            NavigationCommand.To(LoginFragmentDirections.actionLoginFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.bgDescription()
    }

    private fun navigateToEmailConfirmation(email: String) {
        navigationCommand.value = NavigationCommand.To(
            LoginFragmentDirections.actionLoginFragmentToEmailConfirmationFragment(email)
        )
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