package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.databinding.FragmentTestActivityPlaceholderBinding

class TestActivityPlaceholderFragment : BaseFragment() {
    override val viewModel: TestActivityPlaceholderViewModel
        get() = ViewModelProvider(this).get(TestActivityPlaceholderViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestActivityPlaceholderBinding
            .inflate(inflater, container, false)

        return binding.root
    }
}