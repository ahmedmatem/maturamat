package com.ahmedmatem.android.matura.ui.camera

import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class BaseCameraViewModel: BaseViewModel() {
    private val test2Repo: Test2Repository by inject(Test2Repository::class.java)

    private val _isCameraShutterVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCameraShutterVisible = _isCameraShutterVisible.asStateFlow()

    fun savePhoto(id: String, photoUri: String){
        viewModelScope.launch {
            // todo: save photo details in local database
            // ...
        }
    }

    fun runCaptureEffect(durationInMillis: Long) {
        viewModelScope.launch {
            _isCameraShutterVisible.value = true
            delay(durationInMillis)
            _isCameraShutterVisible.value = false
        }
    }
}