package com.ahmedmatem.android.matura.ui.test2

import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand

class Test2ListViewModel : BaseViewModel() {

    fun createNewTest2() {
        navigationCommand.value = NavigationCommand.To(
            Test2ListFragmentDirections.actionTest2ListFragmentToNewTest2Fragment()
        )
    }
}