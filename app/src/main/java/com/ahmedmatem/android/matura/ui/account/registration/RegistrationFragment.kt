package com.ahmedmatem.android.matura.ui.account.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentRegistrationBinding

class RegistrationFragment : BaseFragment() {
    override lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, RegistrationViewModel.Factory(requireContext())).get(
            RegistrationViewModel::class.java
        )

        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}