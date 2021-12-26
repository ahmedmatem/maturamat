package com.ahmedmatem.android.matura.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)

    private val _onLogout = MutableLiveData<Boolean>()
    val onLogout: LiveData<Boolean> = _onLogout

    private val _isAccountActive = MutableLiveData(false)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User>()
    val user: LiveData<UserPrefs.User> = _user

    init {
        _user.value = _userPref.getUser()
        _isAccountActive.value = _user != null
    }

    fun logout() {
        _isAccountActive.value = false
        _userPref.logout()
        _onLogout.value = true
    }

    private fun googleSignOut() {
        TODO("Not yet implemented")
    }

    fun login() {
        _isAccountActive.value = true
    }
}