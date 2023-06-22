package com.ahmedmatem.android.matura.ui.test2

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.network.models.create
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class Test2PlaceholderViewModel: BaseViewModel() {

    private val test2Repo: Test2Repository by inject(Test2Repository::class.java)

    private var requestTimeoutCount: Byte = 0

    private val _startTestBtnEnabledState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val startTestBtnEnabledState: StateFlow<Boolean> = _startTestBtnEnabledState.asStateFlow()

    private lateinit var mockTest: Test2

    init {
        getMockTest()
    }

    private fun getMockTest() {
        viewModelScope.launch {
            test2Repo.getMockTest().collect {
                when(it) {
                    is Result.Success -> {
                        // Enable button launching test after mock test is retrieved.
                        _startTestBtnEnabledState.value = true
                        mockTest = it.data
                    }
                    is Result.GenericError -> {}
                    is Result.NetworkError -> onNetworkError()
                }
            }
        }
        showLoading.value = false
    }

    fun startTest() {
        // Create and save test in local database
        viewModelScope.launch {
            val test = mockTest.create()
            test2Repo.insert(test)
        }
        // navigate to newly created test
        navigationCommand.value = NavigationCommand.To(
            Test2PlaceholderFragmentDirections.actionTest2PlaceholderToNewTest2Fragment(mockTest)
        )
    }

    /**
     * Most probable reason to get Network Error is Request Time Exception.
     * According this assumption try requesting server once more before
     * informing of user with error message.
     */
    private fun onNetworkError() {
        if(requestTimeoutCount < RELOAD_ATTEMPTS) {
            requestTimeoutCount++
            Log.d(TAG, "onNetworkError: Make new request.")
            // make new request to the server
            getMockTest()
        } else {
            // TODO: Implement Network Error
        }
    }

    companion object {
        const val TAG: String = "Test2PlaceholderVM"
        const val RELOAD_ATTEMPTS = 1; // only once
    }
}