package com.ahmedmatem.android.matura.ui.general

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment

private const val ARG_MILLIS_IN_FUTURE = "millis_in_future"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CountDownTimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountDownTimerFragment : BaseFragment() {
    override val viewModel: CountDownTimerViewModel
        get() = ViewModelProvider(this).get(CountDownTimerViewModel::class.java)

    private var millisInFuture: Long? = null
    private lateinit var countdownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millisInFuture = it.getLong(ARG_MILLIS_IN_FUTURE)
        }
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_count_down_timer, container, false)
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
}