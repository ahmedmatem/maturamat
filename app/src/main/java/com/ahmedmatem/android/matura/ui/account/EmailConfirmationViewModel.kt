package com.ahmedmatem.android.matura.ui.account

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import java.lang.IllegalArgumentException

class EmailConfirmationViewModel(private val args: EmailConfirmationFragmentArgs) :
    BaseViewModel() {
    val email = args.email

    private val _navigateToEmailClient = MutableLiveData<Intent>()
    val navigateToEmailClient: LiveData<Intent> = _navigateToEmailClient

    fun navigateToEmailClient() {
        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _navigateToEmailClient.value = intent
    }

    fun sendEmailConfirmationLink() {
        
    }

    class Factory(private val args: EmailConfirmationFragmentArgs) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EmailConfirmationViewModel::class.java)) {
                return EmailConfirmationViewModel(args) as T
            }
            throw IllegalArgumentException("Unable to construct a emailConfirmationViewModel")
        }

    }
}