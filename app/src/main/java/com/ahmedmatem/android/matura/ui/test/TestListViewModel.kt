package com.ahmedmatem.android.matura.ui.test

import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.prizesystem.PrizeWorkManager
import com.ahmedmatem.android.matura.repository.CoinRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TestListViewModel : BaseViewModel() {

    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val coinRepo: CoinRepository by inject(CoinRepository::class.java)
    private val prizeWorkManager: PrizeWorkManager by inject(PrizeWorkManager::class.java)
    private val userPrefs by inject<UserPrefs>(UserPrefs::class.java)

    private val _onTestItemClick = MutableLiveData<Test?>().apply { value = null }
    val onTestItemClick: LiveData<Test?> = _onTestItemClick

    /**
     * Use thees properties in App FREE distribution
     */
    private val _isFabVisible = MutableLiveData<Boolean>(false)
    val isFabVisible: LiveData<Boolean> = _isFabVisible

    // Read data from local database
    val testList = testRepo.getTestList()

    fun refreshTestById(testId: String) {
        viewModelScope.launch {
            testRepo.refreshTestById(testId)
        }
    }

    /**
     * This function will insert/update last test in local db.
     */
    fun refreshLastTest() {
        viewModelScope.launch {
            testRepo.refreshLastTest()
        }
    }

    fun onTestItemClick(test: Test) {
        _onTestItemClick.value = test
    }

    // FREE distribution code

    fun bet() {
        viewModelScope.launch {
            with(coinRepo) {
                val coin = getCoin()
                coin?.let {
                    it.bet() // bet 1 coin for new test
                    update(it) // update local database
                    prizeWorkManager.syncRemote(it) // sync prize at remote database
                }
            }
        }
    }

    fun setFabVisibility() {
        if (userPrefs.isGuest()) {
            // Guest
            _isFabVisible.value = true
        } else {
            // User
            viewModelScope.launch {
                val coin = coinRepo.getCoin()
                _isFabVisible.value = coin?.let {
                    it.total > 0
                } ?: false
            }
        }
    }
}