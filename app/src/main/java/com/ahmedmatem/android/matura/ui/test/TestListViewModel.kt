package com.ahmedmatem.android.matura.ui.test

import android.util.Log
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TestListViewModel : BaseViewModel() {

    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val coinPrizeRepository: CoinPrizeRepository by inject(CoinPrizeRepository::class.java)

    private val _onTestItemClick = MutableLiveData<Test?>().apply { value = null }
    val onTestItemClick: LiveData<Test?> = _onTestItemClick

    /**
     * Use thees properties in App FREE distribution
     */
    private val _isFabVisible = MutableLiveData<Boolean>(false)
    val isFabVisible: LiveData<Boolean> = _isFabVisible

    val coin: LiveData<Int> = Transformations.map(coinPrizeRepository.getCoin()!!) {
        it?.total ?: 0
    }

    init {
        // Refresh test list in local database from network
        Log.d("DEBUG", "Init starts: ")
        refreshTestList()
    }

    // Read data from local database
    val testList = testRepo.testList

    /**
     * Invoke this function from fragment onStart to refresh test list
     */
    private fun refreshTestList() {
        viewModelScope.launch {
            testRepo.refreshTestList()
        }
    }

    fun onTestItemClick(test: Test) {
        _onTestItemClick.value = test
    }

    /*
     *
     * FREE distribution code
     *
     */

    fun bet() {
        viewModelScope.launch {
            coinPrizeRepository.apply {
                val prize = getPrize()
                prize?.let {
                    it.coin.bet() // bet 1 coin for new test
                    update(prize)
                }
            }
        }
    }

    fun setFabVisibility(totalCoin: Int) {
        _isFabVisible.value = totalCoin > 0
    }

    /*class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestListViewModel::class.java)) {
                return TestListViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewModel")
        }

    }*/
}