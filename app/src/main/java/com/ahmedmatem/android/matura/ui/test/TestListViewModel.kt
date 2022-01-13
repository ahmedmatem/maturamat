package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TestListViewModel(val context: Context) : BaseViewModel() {

    private val testRepository = TestRepository(context, MaturaDb.getInstance(context))

    private val _onTestItemClick = MutableLiveData<Int?>().apply { value = null }
    val onTestItemClick: LiveData<Int?> = _onTestItemClick

    // Refresh test list in local database from network
    init {
        viewModelScope.launch {
            testRepository.refreshTestList()
        }
    }

    // Read data from local database
    val testList = testRepository.testList

    fun onTestItemClick(test: Test) {
        _onTestItemClick.value = test.state
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