package com.ahmedmatem.android.matura.ui.account

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class EmailConfirmationViewModel(
    private val context: Context,
    private val args: EmailConfirmationFragmentArgs
) : BaseViewModel() {
    val email = args.email

    private val _accountRepository: AccountRepository by lazy {
        AccountRepository(MaturaDb.getInstance(context).accountDao, AccountApi.retrofitService)
    }

    private val _emailConfirmationLinkSent: MutableLiveData<Boolean> = MutableLiveData()
    val emailConfirmationLinkSent: LiveData<Boolean> = _emailConfirmationLinkSent

    private val _navigateToEmailClient = MutableLiveData<Intent>()
    val navigateToEmailClient: LiveData<Intent> = _navigateToEmailClient

    fun navigateToEmailClient() {
        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _navigateToEmailClient.value = intent
    }

    fun requestEmailConfirmationLink() {
        showLoading.value = true
        viewModelScope.launch {
            when (val result = _accountRepository.requestEmailConfirmationLink(email)) {
                is Result.Success -> onSuccess()
                is Result.GenericError -> onGenericError(result)
                is Result.NetworkError -> onNetworkError()
            }
            showLoading.value = false
        }
    }

    /**
     * On success request FCM Registration token and update it on the Server.
     * Token is used for Email Confirm notification send from Firebase Messaging Cloud.
     */
    private fun onSuccess() {
        _emailConfirmationLinkSent.value = true
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    viewModelScope.launch {
                        _accountRepository.sendFcmRegistrationToServer(email, token)
                    }
                }
            })
    }

    private fun onNetworkError() {
        navigationCommand.value = NavigationCommand.To(
            EmailConfirmationFragmentDirections.actionEmailConfirmationFragmentToNoConnectionFragment()
        )
    }

    private fun onGenericError(response: Result.GenericError) {
        showSnackBar.value = response.errorResponse?.description
    }

    class Factory(
        private val context: Context,
        private val args: EmailConfirmationFragmentArgs
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EmailConfirmationViewModel::class.java)) {
                return EmailConfirmationViewModel(context, args) as T
            }
            throw IllegalArgumentException("Unable to construct a emailConfirmationViewModel")
        }

    }
}