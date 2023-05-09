package com.ahmedmatem.android.matura.ui.account.login.external

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.bgDescription
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.models.withPassword
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class ConfirmAccountViewModel(
    private val context: Context,
    private val args: ConfirmAccountFragmentArgs
) : BaseViewModel() {
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)

    private val _accountRepository by lazy {
        AccountRepository(
            MaturaDb.getInstance(context).accountDao,
            AccountApi.retrofitService
        )
    }

    private val _loginAttemptResult = MutableLiveData<Boolean>()
    val loginAttemptResult: LiveData<Boolean>
        get() = _loginAttemptResult

    val confirmAccountText: LiveData<String> = MutableLiveData<String>(
        context.getString(
            R.string.confirm_account_text,
            args.loginProvider,
            args.email
        )
    )
    val password = MutableLiveData<String>("")
    val email: LiveData<String> = MutableLiveData<String>(args.email)

    fun confirmAccount() {
        showLoading.value = true
        viewModelScope.launch {
            when (val tokenResponse =
                _accountRepository.requestToken(email.value!!, password.value!!)) {
                is Result.Success -> {
                    login(tokenResponse.data.withPassword(password.value))
                }
                is Result.NetworkError -> onNetworkError()
                is Result.GenericError -> onGenericError(tokenResponse)
            }
            showLoading.value = false
        }
    }

    private suspend fun login(user: User) {
        _accountRepository.saveUserLocal(user)
        _userPrefs.setUser(user.username, password.value, user.token)
        _loginAttemptResult.value = true
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand
            .To(ConfirmAccountFragmentDirections.actionConfirmAccountFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.bgDescription()
    }

    class Factory(private val context: Context, private val args: ConfirmAccountFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmAccountViewModel::class.java)) {
                return ConfirmAccountViewModel(context, args) as T
            }
            throw IllegalArgumentException("Unable to construct ConfirmAccountViewModel")
        }
    }
}