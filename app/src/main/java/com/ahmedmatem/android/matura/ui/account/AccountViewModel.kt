package com.ahmedmatem.android.matura.ui.account

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import com.ahmedmatem.android.matura.prizesystem.models.total
import com.ahmedmatem.android.matura.repository.PrizeRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)
    private val prizeRepository: PrizeRepository by inject(PrizeRepository::class.java)

    private val _onLogout = MutableLiveData<Boolean>()
    val onLogout: LiveData<Boolean> = _onLogout

    private val _isAccountActive = MutableLiveData(_userPref.getUser() != null)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User?>(_userPref.getUser())
    val user: LiveData<UserPrefs.User?> = _user

    val totalCoin: LiveData<Int>? =
        user?.value?.username?.let {
            Transformations.map(prizeRepository.getCoin()!!) {
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