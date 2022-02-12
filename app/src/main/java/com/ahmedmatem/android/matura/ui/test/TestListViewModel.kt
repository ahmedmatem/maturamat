package com.ahmedmatem.android.matura.ui.test

import androidx.lifecycle.*
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.infrastructure.FlavorDistribution
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.repository.PrizeRepository
import com.ahmedmatem.android.matura.repository.TestRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TestListViewModel : BaseViewModel() {

    private val testRepo: TestRepository by inject(TestRepository::class.java)
    private val prizeRepo: PrizeRepository by inject(PrizeRepository::class.java)

    private val _onTestItemClick = MutableLiveData<Test?>().apply { value = null }
    val onTestItemClick: LiveData<Test?> = _onTestItemClick

    init {
        /*// Refresh test list in local database from network
        refreshTestList()*/

        // Get prize info in free distribution
        if (BuildConfig.FLAVOR_distribution == FlavorDistribution.FREE) {

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

    /*class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestListViewModel::class.java)) {
                return TestListViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewModel")
        }

    }*/
}