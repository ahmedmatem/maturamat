package com.ahmedmatem.android.matura.ui.test

import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.ui.test.contracts.TestState

class TestActivityPlaceholderViewModel : BaseViewModel() {

    fun navigateByTestState(testState: Int) {
        when (testState) {
            TestState.NOT_STARTED -> navigationCommand.value = NavigationCommand
                .To(TestActivityPlaceholderFragmentDirections.actionPlaceholderToNewTestFragment())
            TestState.INCOMPLETE -> {}
            TestState.COMPLETE -> {}
        }
    }
}