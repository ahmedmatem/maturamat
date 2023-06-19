package com.ahmedmatem.android.matura.ui.test2

import com.ahmedmatem.android.matura.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProblemFragmentTabViewModel : BaseViewModel() {

    private var _solutionCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val solutionCount: StateFlow<Int> = _solutionCount.asStateFlow()

    private var _reloadProblem: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reloadProblem: StateFlow<Boolean> = _reloadProblem.asStateFlow()

    fun reloadProblem() {
        _reloadProblem.value = true
    }

    fun onProblemReload(){
        _reloadProblem.value = false;
    }
}