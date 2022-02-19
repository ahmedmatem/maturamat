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

    // Read data from local database
    val testList = testRepo.getTestList()

    /**
     * This function will update local db.
     * Use it in fragment onResume.
     * In case of creating new test it will be inserting thanks to this function.
     */
    fun refreshLastTest() {
        viewModelScope.launch {
            testRepo.refreshLastTest()
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
}