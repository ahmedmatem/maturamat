package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.ui.general.CountDownTimerFragment
import com.ahmedmatem.lib.mathkeyboard.MathInputEditorFragment

class TestBottomSheetFragment : BaseFragment() {
    override val viewModel: TestViewViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)

                // Add keyboard
                add<MathInputEditorFragment>(R.id.keyboard_container)

                // Add timer
                if (viewModel.hasTimer) {
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