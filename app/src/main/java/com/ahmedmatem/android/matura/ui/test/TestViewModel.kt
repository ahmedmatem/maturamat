package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.*
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.repository.TestRepository
import com.ahmedmatem.android.matura.ui.auth.login.LoginViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TestViewModel(val context: Context) : BaseViewModel() {

    private val testRepository = TestRepository(context, MaturaDb.getInstance(context))

    // Refresh test list in local database from network
    init {
        viewModelScope.launch {
            testRepository.refreshTestList()
        }
    }

    // Read data from local database
    val testList = testRepository.testList

    private val _text = MutableLiveData<String>().apply {
        value = "This is Test Fragment"
    }
    val text: LiveData<String> = _text

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
                return TestViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewModel")
        }

    }
}