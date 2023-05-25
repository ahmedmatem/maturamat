package com.ahmedmatem.android.matura.ui.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST_ID
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestPlaceholderBinding

/**
 * Use placeholder fragment to navigate to a specific test fragment
 * depends on test state value put in intent extra.
 */
class TestPlaceholderFragment : BaseFragment() {
    override val viewModel: TestPlaceholderViewModel
        get() = ViewModelProvider(
            this).get(TestPlaceholderViewModel::class.java)
//    override val viewModel: TestPlaceholderViewModel by viewModels()

    private val testViewModel: TestViewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestPlaceholderBinding
            .inflate(inflater, container, false)
        binding.lifecycleOwner = this


        /**
         * Extra_test contains information about test itself.
         * If navigate back on nav graph to place_holder fragment the
         * value of the extra_test will be null. If it is null -
         * finish TestActivity.
         */
        val intent = requireActivity().intent
        if (intent.hasExtra(EXTRA_TEST)) {
            val bundle = intent.extras
            bundle?.let { it ->
                val test = TestPlaceholderFragmentArgs.fromBundle(it).test
                intent.replaceExtras(null) // remove extras
                viewModel.navigateByTest(test)
            }
        } else {
            /**
             * Finish TestActivity by passing testId as Extra to launcher TestListFragment
             */
            val test = testViewModel.test
            var bundle: Bundle? = null
            test?.let {
                bundle = bundleOf(EXTRA_TEST_ID to it.id)
            } ?: run {
                bundle = bundleOf(EXTRA_TEST_ID to null)
            }
            requireActivity().apply {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtras(bundle!!)
                })
                finish()
            }
        }

        return binding.root
    }
}