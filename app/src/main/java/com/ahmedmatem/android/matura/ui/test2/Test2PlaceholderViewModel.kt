package com.ahmedmatem.android.matura.ui.test2

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class Test2PlaceholderViewModel: BaseViewModel() {

    private val test2Repo: Test2Repository by inject(Test2Repository::class.java)

    fun createTest2() {
        viewModelScope.launch {
            test2Repo.createTest2().collect {
                when(it) {
                    is Result.Success -> onCreateTest2(it.data)
                    is Result.GenericError -> {}
                    is Result.NetworkError -> {}
                }
            }
        }
    }

    private fun onCreateTest2(test: Test2) {
        Log.d(TAG, "onCreateTest2: ${test.id}, ${test.createdOn}")
    }

    companion object {
        const val TAG: String = "Test2PlaceholderVM"
    }
}