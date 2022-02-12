package com.ahmedmatem.android.matura.ui.test

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.infrastructure.FlavorDistribution
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.prizesystem.models.total
import com.ahmedmatem.android.matura.repository.PrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TestListViewModel : BaseViewModel() {

    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val prizeRepo: PrizeRepository by inject(PrizeRepository::class.java)

    private val _onTestItemClick = MutableLiveData<Test?>().apply { value = null }
    val onTestItemClick: LiveData<Test?> = _onTestItemClick

    /**
     * Use thees properties in App FREE distribution
     */
    private val _isFabVisible = MutableLiveData<Boolean>(false)
    val isFabVisible: LiveData<Boolean> = _isFabVisible

    init {
        /*// Refresh test list in local database from network
        refreshTestList()*/

        // Set Fab visibility for app free distribution
        if (BuildConfig.FLAVOR_distribution != FlavorDistribution.FREE) {
            setFabVisibility()
        }
    }

    // Read data from local database
    val testList = testRepo.testList

    /**
     * Invoke this function from fragment onStart to refresh test list
     */
    fun refreshTestList() {
        viewModelScope.launch {
            testRepo.refreshTestList()
        }
    }

    fun onTestItemClick(test: Test) {
        _onTestItemClick.value = test
    }

    /**
     * Use this function to set visibility of Fab button in app free distribution.
     * Depending of user prize button should be hide or visible in order
     * of restriction user of creating unlimited new tests.
     */
    private fun setFabVisibility() {
        viewModelScope.launch {
            val prize = prizeRepo.getPrize()
            prize?.let {
                val coin = it.coin
                _isFabVisible.value = coin.total() > 0
            }
        }
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