package com.ahmedmatem.android.matura.ui.test2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
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

    fun reloadProblem() {
        _reloadProblem.value = true
    }

    fun onProblemReload() {
        _reloadProblem.value = false
    }

    fun navigateToBaseCameraFragment() {
        navigationCommand.value = NavigationCommand.To(
            NewTest2FragmentDirections.actionNewTest2FragmentToBaseCameraFragment()
        )
    }

    fun navigateToSolutionsReviewFragment(uris: String) {
        navigationCommand.value = NavigationCommand.To(
            NewTest2FragmentDirections.actionNewTest2FragmentToSolutionsReviewFragment(uris)
        )
    }

    fun getSolutions() : Flow<List<String>> =
        test2Repo.getProblemSolutions(testId, problemNumber).map {
            it?.split(",")?.toList() ?: emptyList()
        }

    fun setSolutionsCount(count: Int) {
        _solutionCount.value = count
    }

    @Suppress("UNCHECKED_CAST")
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