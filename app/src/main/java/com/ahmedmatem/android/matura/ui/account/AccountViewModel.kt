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

    private val _isAccountActive = MutableLiveData(_userPref.getUser() != null)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User?>(_userPref.getUser())
    val user: LiveData<UserPrefs.User?> = _user

    fun logout() {
        _isAccountActive.value = false
        _userPref.logout()
        _onLogout.value = true
    }

    fun updateAccountActive() {
        _isAccountActive.value = _userPref.getUser() != null
    }

    private fun googleSignOut() {
        TODO("Not yet implemented")
    }
}