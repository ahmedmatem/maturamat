package com.ahmedmatem.android.matura.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)
    private val _isAccountActive = MutableLiveData(false)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    init {
        val user = _userPref.getUser()
        _isAccountActive.value = user != null
    }

    fun logout() {
        _isAccountActive.value = false
        _userPref.logout()
    }

    fun login() {
        _isAccountActive.value = true
    }
}