package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.ui.general.CountDownTimerFragment

class TestBottomSheetFragment : BaseFragment() {
    override val viewModel: TestViewViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add timer if required
        if (viewModel.hasTimer) {
            if (savedInstanceState == null) {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<CountDownTimerFragment>(R.id.timer_container)
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
}