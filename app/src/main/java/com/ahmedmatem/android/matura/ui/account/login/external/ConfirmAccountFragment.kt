package com.ahmedmatem.android.matura.ui.account.login.external

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentConfirmAccountBinding

class ConfirmAccountFragment : BaseFragment() {
    override lateinit var viewModel: ConfirmAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ConfirmAccountViewModel.Factory(requireContext()))
            .get(ConfirmAccountViewModel::class.java)

        val binding = FragmentConfirmAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}