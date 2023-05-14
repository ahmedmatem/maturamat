package com.ahmedmatem.android.matura.ui.account

import android.content.Intent
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.repository.AccountRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class EmailConfirmationViewModel(args: EmailConfirmationFragmentArgs) : BaseViewModel() {
    val email = args.email

    private val _accountRepository: AccountRepository by inject(AccountRepository::class.java)

    private val _emailConfirmationLinkSent: MutableLiveData<Boolean> = MutableLiveData()
    val emailConfirmationLinkSent: LiveData<Boolean> = _emailConfirmationLinkSent

    private val _navigateToEmailClient = MutableLiveData<Intent>()
    val navigateToEmailClient: LiveData<Intent> = _navigateToEmailClient

    fun navigateToEmailClient() {
        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _navigateToEmailClient.value = intent
    }

    fun sendEmailConfirmationLink() {
        showLoading.value = true
        viewModelScope.launch {
            _accountRepository.sendEmailConfirmationLink(email).collect {result ->
                showLoading.value = false
                when(result) {
                    is Result.Success -> {
                        _emailConfirmationLinkSent.value = true
                        launch { onSuccess() }
                    }
                    is Result.GenericError -> onGenericError(result)
                    is Result.NetworkError -> onNetworkError()
                }
            }
        }
    }

    /**
     * On success request FCM Registration token and update it on the Server.
     * Token is used for Email Confirm notification send from Firebase Messaging Cloud.
     */
    private suspend fun onSuccess() {
        _emailConfirmationLinkSent.value = true
        _accountRepository.fcmToken().collect { fcmToken ->
            _accountRepository.updateFcmToken(email, fcmToken)
        }
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand.To(
            EmailConfirmationFragmentDirections.actionEmailConfirmationFragmentToNoConnectionFragment()
        )
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.description
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val args: EmailConfirmationFragmentArgs
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EmailConfirmationViewModel::class.java)) {
                return EmailConfirmationViewModel(args) as T
            }
            throw IllegalArgumentException("Unable to construct a emailConfirmationViewModel")
        }

    }
}