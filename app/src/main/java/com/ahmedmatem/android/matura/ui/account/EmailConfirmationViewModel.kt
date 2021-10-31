package com.ahmedmatem.android.matura.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import java.lang.IllegalArgumentException

class EmailConfirmationViewModel(private val args: EmailConfirmationFragmentArgs) : BaseViewModel() {
    val email = args.email

    class Factory(private val args: EmailConfirmationFragmentArgs) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EmailConfirmationViewModel::class.java)) {
                return EmailConfirmationViewModel(args) as T
            }
            throw IllegalArgumentException("Unable to construct a emailConfirmationViewModel")
        }

    }
}