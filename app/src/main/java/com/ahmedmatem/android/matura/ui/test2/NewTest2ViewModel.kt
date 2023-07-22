package com.ahmedmatem.android.matura.ui.test2

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class NewTest2ViewModel(private val test2Id: String): BaseViewModel() {

    private val test2Repository: Test2Repository by inject(Test2Repository::class.java)

    private val _onTest2Initialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onTest2Initialized = _onTest2Initialized.asStateFlow()

    private val _test2State: MutableStateFlow<Test2?> = MutableStateFlow(null)
    val test2State: StateFlow<Test2?> = _test2State.asStateFlow()

    private val _uploadProgressState: MutableStateFlow<MutableList<Int>> = MutableStateFlow(mutableListOf(0, 0, 0))
    val uploadProgressState: StateFlow<MutableList<Int>> = _uploadProgressState.asStateFlow()

    private var _currentProblemNumber = 1

    init {
        /** Initialize test from local database */
        viewModelScope.launch {
            test2Repository.getTest2ById(test2Id).collect {
                _test2State.value = it
                _onTest2Initialized.value = true
            }
        }
    }

    fun savePhotoInDb(photoUri: Uri) {
        viewModelScope.launch {
            val currentProblemSolutions: String? = when(_currentProblemNumber) {
                1 -> _test2State.value?.firstSolutions
                2 -> _test2State.value?.secondSolutions
                3 -> _test2State.value?.thirdSolutions
                else -> null
            }
            test2Repository.updateSolutions(
                test2Id,
                _currentProblemNumber,
                photoUri.toString(),
                currentProblemSolutions
            )
        }
    }

    /**
     * Submit test2 to the server
     */
    fun submit() {
        // todo: implement file upload procedure
        uploadSolutions()

    }

    fun onProblemChanged(problemNumber: Int) {
        _currentProblemNumber = problemNumber
    }

    private fun uploadSolutions() {
        _test2State.value?.firstSolutions?.let {
            uploadSolution(it, 0)
        }
        _test2State.value?.secondSolutions?.let {
            uploadSolution(it, 1)
        }
        _test2State.value?.thirdSolutions?.let {
            uploadSolution(it, 2)
        }
    }

    /**
     * problemNumber argument is a 0 based index of the problem.
     */
    private fun uploadSolution(solutionsPaths: String, problemNumber: Int) {
        solutionsPaths.split(",").forEach { sp ->
            test2Repository.uploadSolution(sp)
                .addOnSuccessListener {
                    showToast.value = "Solution <$problemNumber> uploaded!"
                }
                .addOnFailureListener { e ->
                    showToast.value = "Failed: ${e.message}"
                }
                .addOnProgressListener { taskSnapshot ->
                    val uploaded =  (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                            as Number
                    _uploadProgressState.value[problemNumber] = uploaded.toInt()
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val test2Id: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewTest2ViewModel::class.java)) {
                return NewTest2ViewModel(test2Id) as T
            }
            throw IllegalArgumentException("unable to construct NewTest2ViewModel")
        }
    }

    companion object {
        const val TAG = "NewTest2ViewModel"
    }
}