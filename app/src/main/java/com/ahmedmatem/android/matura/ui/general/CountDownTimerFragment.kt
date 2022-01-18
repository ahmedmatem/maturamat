package com.ahmedmatem.android.matura.ui.general

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentCountDownTimerBinding
import com.ahmedmatem.android.matura.utils.TestCountDownTimer

private const val ARG_MILLIS_IN_FUTURE = "millis_in_future"

/**
 * A simple [Fragment] subclass.
 * Use the [CountDownTimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountDownTimerFragment : BaseFragment(), TestCountDownTimer.TimerListener {

    override val viewModel: CountDownTimerViewModel
        get() = ViewModelProvider(this).get(CountDownTimerViewModel::class.java)

    private var millisInFuture: Long? = null
    private lateinit var timer: TestCountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millisInFuture = it.getLong(ARG_MILLIS_IN_FUTURE)
        }
        setupTimer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCountDownTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.time = viewModel.time

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(millisInFuture: Long) =
            CountDownTimerFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_MILLIS_IN_FUTURE, millisInFuture)
                }
            }
    }

    private fun setupTimer() {
        millisInFuture = millisInFuture
            ?: requireContext().resources.getInteger(R.integer.test_duration_in_minutes) as Long
        viewModel.time = millisInFuture.toString()
        timer = TestCountDownTimer.create(millisInFuture!!, this)
    }

    override fun onTimerTick(millisInFuture: Long) {
        viewModel.time = millisInFuture.toString()
    }

    override fun onTimerFinish() {
        TODO("Not yet implemented")
    }
}