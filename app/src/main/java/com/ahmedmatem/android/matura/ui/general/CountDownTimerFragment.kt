package com.ahmedmatem.android.matura.ui.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        timerViewModel = CountDownTimerViewModel(viewModel.test?.millisInFuture)

        val binding = FragmentCountDownTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        timerViewModel.millisInFuture.observe(viewLifecycleOwner, Observer { millis ->
            millis?.let {
                binding.time = TimeConverter.from(it).toTimerString()
            }
        })

        viewModel.onTimerResume.observe(viewLifecycleOwner, Observer { millisInFuture ->
            millisInFuture?.let {
                timerViewModel.timer.resume()
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        timerViewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
        timerViewModel.onResume()
    }


}