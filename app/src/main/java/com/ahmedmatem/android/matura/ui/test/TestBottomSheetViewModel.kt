package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test
import java.lang.IllegalArgumentException

class TestBottomSheetViewModel(
    private val context: Context,
    val test: Test?
) : BaseViewModel() {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val isCardsViewMode: Boolean
        get() {
            val cardsViewValue = context.getString(R.string.test_view_cards)
            val testViewKey = context.getString(R.string.test_view_key)
            val testViewDefault = context.getString(R.string.test_view_default)
            val currentView = prefs.getString(testViewKey, testViewDefault)
            return currentView == cardsViewValue
        }

    val isListViewMode = !isCardsViewMode

    val hasTimer =
        test?.hasTimer ?: prefs.getBoolean(context.getString(R.string.timer_key), true)

    val millisInFuture: Long =
        test?.millisInFuture
            ?: context.resources.getInteger(R.integer.test_duration_in_minutes) * 60 * 1000L

    class Factory(private val context: Context, private val test: Test?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestBottomSheetViewModel::class.java)) {
                return TestBottomSheetViewModel(context, test) as T
            }
            throw IllegalArgumentException("Can not create TestBottomSheetViewModel.")
        }

    }
}