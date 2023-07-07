package com.ahmedmatem.android.matura.ui.test2

import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.repository.Test2Repository
import org.koin.java.KoinJavaComponent.inject

class Test2ListViewModel : BaseViewModel() {
    private val test2Repository: Test2Repository by inject(Test2Repository::class.java)

    fun getAll() = test2Repository.getAll()
}