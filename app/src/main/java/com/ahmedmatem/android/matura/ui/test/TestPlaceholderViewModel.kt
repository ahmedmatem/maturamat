package com.ahmedmatem.android.matura.ui.test

import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.infrastructure.FlavorVersion
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import org.koin.java.KoinJavaComponent.inject

class TestPlaceholderViewModel : BaseViewModel() {

    private val testUrlUtil: TestURLUtil by inject(TestURLUtil::class.java)
    private val userPrefs by inject<UserPrefs>(UserPrefs::class.java)

    fun navigateByTest(test: Test?) {
        if (test?.state == TestState.COMPLETE) {
            navigateToTestResult(test.id)
        } else {
            navigateToTest(test)
        }
    }

    private fun navigateToTest(test: Test?) {
        navigationCommand.value = NavigationCommand.To(
            TestPlaceholderFragmentDirections.actionPlaceholderToTestViewFragment(test)
        )
    }

    private fun navigateToTestResult(testId: String) {
        val isNvo4Version: Boolean = BuildConfig.FLAVOR_version == FlavorVersion.NVO4
        val testResultUrl = if (userPrefs.isGuest() && !isNvo4Version) {
            // Show only test result summary for Guest
            testUrlUtil.testResultSummaryOnlyUrl(testId)
        } else {
            testUrlUtil.testResultUrl(testId)
        }
        navigationCommand.value = NavigationCommand.To(
            TestPlaceholderFragmentDirections.actionPlaceholderToTestResultFragment(testResultUrl)
        )
    }

}