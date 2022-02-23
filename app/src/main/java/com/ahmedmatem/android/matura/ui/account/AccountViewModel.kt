package com.ahmedmatem.android.matura.ui.account

import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.ui.test.worker.TestListRefreshWorker
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AccountViewModel : BaseViewModel() {
    private val _userPref: UserPrefs by inject(UserPrefs::class.java)
    private val coinPrizeRepo: CoinPrizeRepository by inject(CoinPrizeRepository::class.java)
    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val workManager: WorkManager by inject(WorkManager::class.java)

    private val _onLogout = MutableLiveData<Boolean>()
    val onLogout: LiveData<Boolean> = _onLogout

    private val _isAccountActive = MutableLiveData(_userPref.getUser() != null)
    val isAccountActive: LiveData<Boolean> = _isAccountActive

    private val _user = MutableLiveData<UserPrefs.User?>(_userPref.getUser())
    val user: LiveData<UserPrefs.User?> = _user

    val totalCoin: LiveData<Int>? =
        user.value?.username?.let {
            Transformations.map(coinPrizeRepo.getCoin()!!) {
                it?.total
            }
        }

    /**
     * Refresh user test list in local database after login if only there is no saved record.
     */
    fun refreshUserTestListIfNecessary() {
        viewModelScope.launch {
            val isEmpty = testRepo.isEmpty()
            // Refresh test list if only it is empty
            if (isEmpty) {
                val testListRefreshRequest = OneTimeWorkRequestBuilder<TestListRefreshWorker>().build()
                workManager.enqueue(testListRefreshRequest)
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