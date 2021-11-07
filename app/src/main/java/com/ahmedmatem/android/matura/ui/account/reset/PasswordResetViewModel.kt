package com.ahmedmatem.android.matura.ui.account.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmedmatem.android.matura.base.BaseViewModel

class PasswordResetViewModel: BaseViewModel() {

    private val _showSendPasswordResetFailMessage = MutableLiveData(false)
    val showSendPasswordResetFailMessage: LiveData<Boolean> = _showSendPasswordResetFailMessage

    private val _failMessage: MutableLiveData<String> = MutableLiveData()
    val failMessage: LiveData<String> = _failMessage
}