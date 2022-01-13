package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestActivityPlaceholderBinding

/**
 * Use placeholder fragment to navigate to a specific test fragment
 * depends on test state value put in intent extra.
 */
class PlaceholderFragment : BaseFragment() {
    override val viewModel: PlaceholderViewModel
        get() = ViewModelProvider(
            this,
            PlaceholderViewModel.Factory(requireContext())
        ).get(PlaceholderViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestActivityPlaceholderBinding
            .inflate(inflater, container, false)

        /**
         * Read extra related to test and
         * remove the extra which will guarantee the next time we get back to placeholder
         * fragment it will trigger TestActivity finishing.
         */
        val intent = requireActivity().intent
        if (intent.hasExtra(EXTRA_TEST)) {
            val bundle = intent.extras
            bundle?.let { it ->
                val test = PlaceholderFragmentArgs.fromBundle(it).test
                intent.replaceExtras(null) // remove extras
                viewModel.navigateByTest(test)
            }
        } else {
            requireActivity().finish()
        }

        return binding.root
    }
}