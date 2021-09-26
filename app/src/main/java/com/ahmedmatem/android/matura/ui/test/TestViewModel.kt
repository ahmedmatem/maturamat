package com.ahmedmatem.android.matura.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Test Fragment"
    }
    val text: LiveData<String> = _text
}