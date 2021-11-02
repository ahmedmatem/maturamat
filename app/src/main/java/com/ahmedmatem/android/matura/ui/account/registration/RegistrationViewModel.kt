package com.ahmedmatem.android.matura.ui.account.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import kotlinx.coroutines.launch

class RegistrationViewModel : BaseViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    init {
        // todo: request FCM registration token
    }

    fun register() {
        if (inputValid()) {

        }
        viewModelScope.launch {

        }
    }

    private fun inputValid(): Boolean {

        return false
    }

}