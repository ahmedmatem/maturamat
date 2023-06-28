package com.ahmedmatem.android.matura

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.repository.Test2Repository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class Test2ActivityViewModel : BaseViewModel() {
    private val test2Repository: Test2Repository by inject(Test2Repository::class.java)

    private var currentProblemNumber:Int = 0
    private var testId: String = ""

    fun savePhotoInDb(photoUri: Uri) {
        viewModelScope.launch {
            test2Repository.updateSolution(testId, currentProblemNumber, photoUri.toString())
        }
    }

    fun setCurrentProblemNumber(number: Int) {
        currentProblemNumber = number
    }

    fun setTestId(testId: String) {
        this.testId = testId
    }
}