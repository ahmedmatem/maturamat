package com.ahmedmatem.android.matura.ui.test2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewTest2ViewModel(private val test: Test2): BaseViewModel() {

    fun logTestId(){
        Log.d(TAG, "showTestId: ${test.id}")
    }

    class Factory(private val test: Test2): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewTest2ViewModel::class.java)) {
                return NewTest2ViewModel(test) as T
            }
            throw IllegalArgumentException("unable to construct NewTest2ViewModel")
        }
    }

    companion object {
        const val TAG = "NewTest2ViewModel"
    }
}