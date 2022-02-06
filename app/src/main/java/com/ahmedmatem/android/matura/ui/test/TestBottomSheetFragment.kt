package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.TestCardsBottomSheetBinding
import com.ahmedmatem.android.matura.databinding.TestListBottomSheetBinding
import com.ahmedmatem.android.matura.ui.general.CountDownTimerFragment
import com.ahmedmatem.lib.mathkeyboard.MathInputEditorFragment
import com.ahmedmatem.lib.mathkeyboard.config.Constants
import com.ahmedmatem.lib.mathkeyboard.contracts.KeyboardExternalListener

class TestBottomSheetFragment : BaseFragment(), KeyboardExternalListener {
    override val viewModel: TestViewViewModel by activityViewModels()

    private lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)

                // Add keyboard
                val keyboardFragment =
                    MathInputEditorFragment.newInstance(this@TestBottomSheetFragment)
                add(R.id.keyboard_container, keyboardFragment, Constants.FRAGMENT_TAG)

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
    ): View {
        binding = if (viewModel.isCardsViewMode) {
            // Card view bottom sheet
            binding = TestCardsBottomSheetBinding.inflate(inflater, container, false)
            (binding as TestCardsBottomSheetBinding)
        } else {
            // List view bottom sheet
            binding = TestListBottomSheetBinding.inflate(inflater, container, false)
            binding as TestListBottomSheetBinding
        }

        viewModel.showKeyboard.observe(viewLifecycleOwner, Observer { show ->
            if (viewModel.isCardsViewMode) {
                (binding as TestCardsBottomSheetBinding).apply {
                    showKeyboard = show
                }
            } else {
                (binding as TestListBottomSheetBinding).apply {
                    showKeyboard = show
                }
            }
        })

        return binding.root
    }

    override fun onKeyboardCloseBtnClick() {
        viewModel.onKeyboardCloseBtnClick()
    }

    override fun onKeyboardSubmit(selector: String?) {
        selector?.let { viewModel.onKeyboardSubmit(it) }
    }

}