package com.ahmedmatem.android.matura.ui.test2

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.repository.Test2Repository
import com.ahmedmatem.android.matura.utils.ManyToOneProgressTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.security.InvalidParameterException

class NewTest2ViewModel(private val test2Id: String): BaseViewModel() {

    private val test2Repository: Test2Repository by inject(Test2Repository::class.java)

    private val _onTest2Initialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onTest2Initialized = _onTest2Initialized.asStateFlow()

    private val _test2State: MutableStateFlow<Test2?> = MutableStateFlow(null)
    val test2State: StateFlow<Test2?> = _test2State.asStateFlow()

    private val _submit: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val submit: StateFlow<Boolean> = _submit.asStateFlow()

    private val _progressState1: MutableStateFlow<Int> = MutableStateFlow(0)
    val progressState1: StateFlow<Int> = _progressState1.asStateFlow()

    private val _progressState2: MutableStateFlow<Int> = MutableStateFlow(0)
    val progressState2: StateFlow<Int> = _progressState2.asStateFlow()

    private val _progressState3: MutableStateFlow<Int> = MutableStateFlow(0)
    val progressState3: StateFlow<Int> = _progressState3.asStateFlow()

    private val _dialogPositiveButtonEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val dialogPositiveButtonEnabled: StateFlow<Boolean> = _dialogPositiveButtonEnabled.asStateFlow()

    /**
     * tracksCount will be used in following progressTracker(#) for initialization of trucks number.
     * Must be positive number - that's why set it before invoke progressTracker(#) lazy creation.
     */
    private var tracksCount = 0
    private val progressTracker1: ManyToOneProgressTracker by lazy {
        ManyToOneProgressTracker.from(tracksCount)
    }
    private val progressTracker2: ManyToOneProgressTracker by lazy {
        ManyToOneProgressTracker.from(tracksCount)
    }
    private val progressTracker3: ManyToOneProgressTracker by lazy {
        ManyToOneProgressTracker.from(tracksCount)
    }

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

    fun getProblemSolutionsCount(problemNumber: Int) : Int {
        return when(problemNumber){
            0 -> _test2State.value?.firstSolutions?.split(",")?.size ?: 0
            1 -> _test2State.value?.secondSolutions?.split(",")?.size ?: 0
            2 -> _test2State.value?.thirdSolutions?.split(",")?.size ?: 0
            else -> 0
        }
    }

    fun onDialogPositiveButtonClick() {
        _submit.value = true
        // Disable positive button
        _dialogPositiveButtonEnabled.value = false
    }

    fun onDialogPositiveButtonClickComplete() {
        _submit.value = false
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
        solutionsPaths.split(",").also {
            // Initialize tracksCount needed for progressTracker lazy creation
            tracksCount = it.size
            // Create progressTracker for solution relevant to problem with given problemNumber
            when(problemNumber) {
                0 -> progressTracker1
                1 -> progressTracker2
                2 -> progressTracker3
            }
        }
        .forEachIndexed { track, path ->
            test2Repository.uploadSolution(path)
                .addOnSuccessListener {
//                    showToast.value = "Solution <$problemNumber> uploaded!"
                }
                .addOnFailureListener { e ->
                    // todo: update UI according to solution upload failure
                    showToast.value = "Failed: ${e.message}"
                }
                .addOnProgressListener { taskSnapshot ->
                    val uploaded =
                        ((100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount))
                            .toInt()
                    updateProgressBarUI(problemNumber, uploaded, track)
                }
            }
    }

    private fun updateProgressBarUI(problemNumber: Int, uploaded: Int, track: Int) {
        when(problemNumber) {
            0 -> {
                progressTracker1.updateProgress(uploaded, track)
                _progressState1.value = progressTracker1.progress
            }
            1 -> {
                progressTracker2.updateProgress(uploaded, track)
                _progressState2.value = progressTracker1.progress
            }
            2 -> {
                progressTracker3.updateProgress(uploaded, track)
                _progressState3.value = progressTracker1.progress
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