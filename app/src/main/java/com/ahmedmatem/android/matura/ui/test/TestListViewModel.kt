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

    private val _navigateByTestState = MutableLiveData<Int?>().apply { value = null }
    val navigateByTestState: LiveData<Int?> = _navigateByTestState

    // Refresh test list in local database from network
    init {
        viewModelScope.launch {
            testRepository.refreshTestList()
        }
    }

    // Read data from local database
    val testList = testRepository.testList

    fun onTestItemClick(test: Test) {
        when (test.state) {
            TestState.NOT_STARTED -> startNewTest()
            TestState.INCOMPLETE -> resumeTest()
            TestState.COMPLETE -> showTestResult()
        }
    }

    private fun startNewTest() {
//        val urlUtil = URLUtil.from(context, ProductFlavor.DZI_12)
//        val newTestUrl = urlUtil.newTestUrl()
//        navigationCommand.value =
//            NavigationCommand.To(TestListFragmentDirections.actionNavigationTestsToNewTestFragment())
        _navigateByTestState.value = TestState.NOT_STARTED
    }

    private fun resumeTest() {
//        navigationCommand.value =
//            NavigationCommand.To(TestListFragmentDirections.actionNavigationTestsToNewTestFragment())
        _navigateByTestState.value = TestState.NOT_STARTED
    }

    private fun showTestResult() {
//        navigationCommand.value =
//            NavigationCommand.To(TestListFragmentDirections.actionNavigationTestsToNewTestFragment())
        _navigateByTestState.value = TestState.NOT_STARTED
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