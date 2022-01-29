package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.repository.TestRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TestListViewModel(val context: Context) : BaseViewModel() {

    private val testRepository = TestRepository(context, MaturaDb.getInstance(context))

    private val _onTestItemClick = MutableLiveData<Test?>().apply { value = null }
    val onTestItemClick: LiveData<Test?> = _onTestItemClick

    // Refresh test list in local database from network
//    init {
//        refreshTestList()
//    }

    // Read data from local database
    val testList = testRepository.testList

    /**
     * Invoke this function from fragment onStart to refresh test list
     */
    fun refreshTestList() {
        viewModelScope.launch {
            testRepository.refreshTestList()
        }
    }

    fun onTestItemClick(test: Test) {
        _onTestItemClick.value = test
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestListViewModel::class.java)) {
                return TestListViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewModel")
        }

    }
}