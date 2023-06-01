package com.ahmedmatem.android.matura.ui.test2

import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class Test2PlaceholderViewModel: BaseViewModel() {

    private val test2Repo: Test2Repository by inject(Test2Repository::class.java)

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
                    is Result.NetworkError -> {}
                }
            }
        }
        showLoading.value = false
    }

    fun startTest() {
        navigationCommand.value = NavigationCommand.To(
            Test2PlaceholderFragmentDirections.actionTest2PlaceholderToNewTest2Fragment(mockTest)
        )
    }

    companion object {
//        const val TAG: String = "Test2PlaceholderVM"
    }
}