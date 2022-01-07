package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST_STATE
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.databinding.FragmentTestActivityPlaceholderBinding
import com.ahmedmatem.android.matura.ui.test.contracts.TestState

/**
 * Use placeholder fragment to redirect to a specific test fragment
 * depends on testState value put in intent extra.
 */
class TestActivityPlaceholderFragment : BaseFragment() {
    override val viewModel: TestActivityPlaceholderViewModel
        get() = ViewModelProvider(this).get(TestActivityPlaceholderViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestActivityPlaceholderBinding
            .inflate(inflater, container, false)

        /**
         * Read extra related to test state and
         * remove the extra which will guarantee the next time we get back to placeholder
         * fragment it will trigger TestActivity finishing.
         */
        val intent = requireActivity().intent
        if (intent.hasExtra(EXTRA_TEST_STATE)) {
            val bundle = intent.extras
            bundle?.let { it ->
                val testState = TestActivityPlaceholderFragmentArgs.fromBundle(it).testState
                intent.replaceExtras(null) // remove extras
                viewModel.navigateByTestState(testState)
            }
        } else {
            requireActivity().finish()
        }

        return binding.root
    }
}