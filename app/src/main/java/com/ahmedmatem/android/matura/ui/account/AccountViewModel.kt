package com.ahmedmatem.android.matura.ui.account

import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.repository.CoinRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.ui.test.worker.TestListRefreshWorker
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)
    private val coinRepo: CoinRepository by inject(CoinRepository::class.java)
    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val workManager: WorkManager by inject(WorkManager::class.java)

    private val _onLogout = MutableLiveData<Boolean>()
    val onLogout: LiveData<Boolean> = _onLogout

    private val _isAccountActive = MutableLiveData(_userPref.getUser() != null)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User?>(_userPref.getUser())
    val user: LiveData<UserPrefs.User?> = _user

    private val _totalCoin: MutableLiveData<Int> = MutableLiveData()
    val totalCoin: LiveData<Int> = _totalCoin

    /**
     * Refresh user test list in local database after login if only there is no saved record.
     */
    fun refreshUserTestListIfNecessary() {
        viewModelScope.launch {
            val isEmpty = testRepo.isEmpty()
            // Refresh test list if only it is empty
            if (isEmpty) {
                val testListRefreshRequest =
                    OneTimeWorkRequestBuilder<TestListRefreshWorker>().build()
                workManager.enqueue(testListRefreshRequest)
            }
        }
    }

    fun refreshTotalCoin() {
        viewModelScope.launch {
            val coin = coinRepo.getCoin()
            coin?.let {
                _totalCoin.value = it.total
            }
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