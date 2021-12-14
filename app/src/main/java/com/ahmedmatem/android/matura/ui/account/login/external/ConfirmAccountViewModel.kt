package com.ahmedmatem.android.matura.ui.account.login.external

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import java.lang.IllegalArgumentException

class ConfirmAccountViewModel(
    private val context: Context,
    private val args: ConfirmAccountFragmentArgs
) : BaseViewModel() {

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

    }

    class Factory(private val context: Context, private val args: ConfirmAccountFragmentArgs) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmAccountViewModel::class.java)) {
                return ConfirmAccountViewModel(context, args) as T
            }
            throw IllegalArgumentException("Unable to construct ConfirmAccountViewModel")
        }
    }
}