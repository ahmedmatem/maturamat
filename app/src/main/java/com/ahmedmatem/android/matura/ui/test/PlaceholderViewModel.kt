package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.infrastructure.FlavorVersion
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class PlaceholderViewModel(private val context: Context) : BaseViewModel() {

    private val urlUtil: TestURLUtil by lazy { TestURLUtil(context) }
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
            PlaceholderFragmentDirections.actionPlaceholderToTestViewFragment(test)
        )
    }

    private fun navigateToTestResult(testId: String) {
        val isNvo4Version: Boolean = BuildConfig.FLAVOR_version == FlavorVersion.NVO4
        val testResultUrl = if (userPrefs.isGuest() && !isNvo4Version) {
            // Show only test result summary for Guest
            urlUtil.testResultSummaryOnlyUrl(testId)
        } else {
            urlUtil.testResultUrl(testId)
        }
        navigationCommand.value = NavigationCommand.To(
            PlaceholderFragmentDirections.actionPlaceholderToTestResultFragment(testResultUrl)
        )
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaceholderViewModel::class.java)) {
                return PlaceholderViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestActivityPlaceholderViewModel")
        }

    }
}