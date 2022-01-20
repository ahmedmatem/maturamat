package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.general.CountDownTimerFragment

private const val ARG_TEST = "test"

class TestBottomSheetFragment : BaseFragment() {

    private var test: Test? = null

    override lateinit var viewModel: TestBottomSheetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            test = it.getParcelable(ARG_TEST)
        }
        viewModel = ViewModelProvider(
            this, TestBottomSheetViewModel.Factory(
                requireContext(), test
            )
        ).get(TestBottomSheetViewModel::class.java)

        // Add timer if required
        if (viewModel.hasTimer) {
            if (savedInstanceState == null) {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(
                        R.id.timer_container,
                        CountDownTimerFragment.newInstance(viewModel.millisInFuture)
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = if (viewModel.isCardsViewMode) {
            // Card view bottom sheet
            inflater.inflate(R.layout.test_cards_bottom_sheet, container, false)
        } else {
            // List view bottom sheet
            inflater.inflate(R.layout.test_list_bottom_sheet, container, false)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(test: Test?) =
            TestBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TEST, test)
                }
            }
    }
}