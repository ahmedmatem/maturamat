package com.ahmedmatem.android.matura.ui.test2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class ProblemFragmentTabViewModel(
    private val testId: String,
    private val problemNumber: Int
) : BaseViewModel() {

    private val test2Repo: Test2Repository by inject(Test2Repository::class.java)

    private var _solutionCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val solutionCount: StateFlow<Int> = _solutionCount.asStateFlow()

    private var _reloadProblem: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reloadProblem: StateFlow<Boolean> = _reloadProblem.asStateFlow()

    private val _solutionListState: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val solutionListState: StateFlow<List<String>> = _solutionListState.asStateFlow()

    init {
        getSolutions(testId, problemNumber)
    }

    fun reloadProblem() {
        _reloadProblem.value = true
    }

    fun onProblemReload() {
        _reloadProblem.value = false;
    }

    fun navigateToBaseCameraFragment() {
        navigationCommand.value = NavigationCommand.To(
            NewTest2FragmentDirections.actionNewTest2FragmentToBaseCameraFragment()
        )
    }

    private fun getSolutions(testId: String, problemNumber: Int) {
        viewModelScope.launch {
            when(problemNumber) {
                1 -> test2Repo.getFirstSolutions(testId).collect {
                    _solutionListState.value = it?.split(',')?.toList() ?: emptyList()
                }
                2 -> test2Repo.getSecondSolutions(testId).collect {
                    _solutionListState.value = it?.split(',')?.toList() ?: emptyList()
                }
                3 -> test2Repo.getThirdSolutions(testId).collect {
                    _solutionListState.value = it?.split(',')?.toList() ?: emptyList()
                }
                else -> Log.e(Test2Repository.TAG, "Invalid problem number $problemNumber")
            }
        }
    }

    class Factory(
        private val testId: String,
        private val problemNumber: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ProblemFragmentTabViewModel::class.java)) {
                return ProblemFragmentTabViewModel(testId, problemNumber) as T
            }
            throw IllegalArgumentException("Unable to construct a ProblemFragmentTabViewModel")
        }
    }
}