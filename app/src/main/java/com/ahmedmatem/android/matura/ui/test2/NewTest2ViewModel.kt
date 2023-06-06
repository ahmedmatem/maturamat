package com.ahmedmatem.android.matura.ui.test2

import com.ahmedmatem.android.matura.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewTest2ViewModel: BaseViewModel() {

    private var _unloadProblemId: MutableStateFlow<String?> = MutableStateFlow(null)
    val unloadProblemId: StateFlow<String?> = _unloadProblemId.asStateFlow()

    fun reloadProblemById(id: String) {
        _unloadProblemId.value = id
    }

    companion object {
        const val TAG = "NewTest2ViewModel"
    }
}