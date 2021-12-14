package com.ahmedmatem.android.matura.ui.account.login.external

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import java.lang.IllegalArgumentException

class ConfirmAccountViewModel(val context: Context) : BaseViewModel() {

    private val _confirmAccountText = MutableLiveData<String>()
    val confirmAccountText: LiveData<String> = _confirmAccountText

    val password = MutableLiveData<String>("")

    init {
        _confirmAccountText.value =
            context.getString(R.string.confirm_account_text, "Гугъл", "ahmedmatem@gmail.com")
    }

    fun confirmAccount() {

    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmAccountViewModel::class.java)) {
                return ConfirmAccountViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct ConfirmAccountViewModel")
        }
    }
}