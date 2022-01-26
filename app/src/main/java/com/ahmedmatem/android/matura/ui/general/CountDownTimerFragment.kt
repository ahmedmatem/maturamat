package com.ahmedmatem.android.matura.ui.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentCountDownTimerBinding
import com.ahmedmatem.android.matura.ui.test.TestViewViewModel
import com.ahmedmatem.android.matura.utils.TimeConverter

class CountDownTimerFragment : BaseFragment() {
    override val viewModel: TestViewViewModel by activityViewModels()
    private lateinit var timerViewModel: CountDownTimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        timerViewModel = CountDownTimerViewModel(viewModel.millisInFuture)

        val binding = FragmentCountDownTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Set timer click listener on timer fragment layout
        binding.apply {
            (root as ConstraintLayout).setOnClickListener {
                viewModel.onTimerClick(timerViewModel.millisInFuture?.value!!)
            }
        }

        timerViewModel.millisInFuture.observe(viewLifecycleOwner, Observer { millis ->
            millis?.let {
                binding.time = TimeConverter.from(it).toTimerString()
                viewModel.millisInFuture = millis // update millis in shared TestViewModel
            }
        })

        timerViewModel.onTimerFinish.observe(viewLifecycleOwner) {
            viewModel.onTimerFinish()
        }

        viewModel.onDialogPositiveClick.observe(viewLifecycleOwner, Observer { tag ->
            tag?.let { timerViewModel.onDialogPositiveClick(tag) }
        })

        viewModel.onDialogNegativeClick.observe(viewLifecycleOwner, Observer { tag ->
            tag?.let { timerViewModel.onDialogNegativeClick(tag) }
        })

        viewModel.onDialogNeutralClick.observe(viewLifecycleOwner, Observer { tag ->
            tag?.let { timerViewModel.onDialogNeutralClick(tag) }
        })

        /**
         * Observe this property to handle 'Home' and 'Check' menu item pressed
         */
        viewModel.onOptionItemSelected.observe(viewLifecycleOwner, Observer { selected ->
            if (selected) {
                timerViewModel.onOptionItemSelected()
            }
        })

        viewModel.onBackPressed.observe(viewLifecycleOwner, Observer { pressed ->
            if (pressed) timerViewModel.onBackPressed()
        })

        viewModel.onTimerCLick.observe(viewLifecycleOwner) {
            timerViewModel.onTimerClick()
        }

        return binding.root
    }

    /**
     * Fragment onPause response
     */
    override fun onPause() {
        super.onPause()
        timerViewModel.onPause()
    }

    /**
     * Fragment onResume response
     */
    override fun onResume() {
        super.onResume()
        timerViewModel.onResume()
    }


}