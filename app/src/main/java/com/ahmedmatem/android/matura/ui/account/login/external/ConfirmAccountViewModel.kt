package com.ahmedmatem.android.matura.ui.account.login.external

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.descriptionBg
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.network.models.withPassword
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class ConfirmAccountViewModel(args: ConfirmAccountFragmentArgs) : BaseViewModel() {
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)

    private val _loginAttemptResult = MutableLiveData<Boolean>()
    val loginAttemptResult: LiveData<Boolean> = _loginAttemptResult

    val confirmAccountText: LiveData<String> = MutableLiveData(
        "За да свържем профила Ви от ${args.loginProvider} с ${args.email} от МатураМат е необходимо да потвърдите, че е ваш."
    )
    val password = MutableLiveData("")
    val email: LiveData<String> = MutableLiveData(args.email)

    fun confirmAccount() {
        showLoading.value = true
        viewModelScope.launch {
            _accountRepository.token(email.value!!, password.value!!).collect { result ->
                when(result) {
                    is Result.Success -> saveUserCredentialsLocal(result.data.withPassword(password.value))
                    is Result.NetworkError -> onNetworkError()
                    is Result.GenericError -> onGenericError(result)
                }
            }
            showLoading.value = false
        }
    }

    private fun saveUserCredentialsLocal(user: User) {
        _accountRepository.save(user)
        _userPrefs.setUser(user.username, password.value, user.token)
        _loginAttemptResult.value = true
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand
            .To(ConfirmAccountFragmentDirections.actionConfirmAccountFragmentToNoConnectionFragment())
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.descriptionBg()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val args: ConfirmAccountFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmAccountViewModel::class.java)) {
                return ConfirmAccountViewModel(args) as T
            }
            throw IllegalArgumentException("Unable to construct ConfirmAccountViewModel")
        }
    }
}