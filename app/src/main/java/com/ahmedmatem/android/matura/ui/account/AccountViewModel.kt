package com.ahmedmatem.android.matura.ui.account

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)
    private val coinPrizeRepository: CoinPrizeRepository by inject(CoinPrizeRepository::class.java)

    private val _onLogout = MutableLiveData<Boolean>()
    val onLogout: LiveData<Boolean> = _onLogout

    private val _isAccountActive = MutableLiveData(_userPref.getUser() != null)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User?>(_userPref.getUser())
    val user: LiveData<UserPrefs.User?> = _user

    val totalCoin: LiveData<Int>? =
        user.value?.username?.let {
            Transformations.map(coinPrizeRepository.getCoin()!!) {
                it?.total
            }
        }

    fun logout() {
        _isAccountActive.value = false
        _userPref.logout()
        _onLogout.value = true
    }

    fun updateAccountActive() {
        _isAccountActive.value = _userPref.getUser() != null
    }
}