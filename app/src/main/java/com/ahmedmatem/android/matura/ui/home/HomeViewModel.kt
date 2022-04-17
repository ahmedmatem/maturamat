package com.ahmedmatem.android.matura.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

    val resources: ResourcesProvider by inject(ResourcesProvider::class.java)

    private val _welcomeText = MutableLiveData<String>().apply {
        value = resources.getResources().getString(R.string.text_welcome)
    }
    val welcomeText: LiveData<String> = _welcomeText
}