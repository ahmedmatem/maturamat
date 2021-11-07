package com.ahmedmatem.android.matura.ui.account.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentPasswordResetBinding

class PasswordResetFragment : BaseFragment() {

    override lateinit var viewModel: PasswordResetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, PasswordResetViewModel.Factory(requireContext())).get(
                PasswordResetViewModel::class.java
            )

        val binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.sendPasswordResetButton.setOnClickListener(View.OnClickListener {
            val email = binding.email.text.toString()
            viewModel.sendPasswordResetEmail(email)
        })

        return binding.root
    }
}