package com.ahmedmatem.android.matura.ui.account.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentPasswordResetBinding
import com.ahmedmatem.android.matura.network.HttpStatus
import com.ahmedmatem.android.matura.ui.account.registration.Error

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

        viewModel.showSuccessMessage.observe(viewLifecycleOwner, Observer { show ->
            if(show) {
                val email = binding.email.text.toString()
                viewModel.setSuccessMessage(getString(R.string.forgot_password_success_message, email))
            }
        })

        viewModel.failCode.observe(viewLifecycleOwner, Observer { code ->
            code?.let {
                when(it) {
                   HttpStatus.NotFound.code -> {
                       val email = binding.email.text.toString()
                       viewModel.setFailMessage(getString(R.string.email_not_found, email))
                   }
                    HttpStatus.BadRequest.code ->
                        viewModel.setFailMessage(getString(R.string.email_required_message))
                    else -> {}
                }
            }
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                when(it) {
                    Error.EMAIL_REQUIRED ->
                        viewModel.setFailMessage(getString(R.string.email_required_message))
                    Error.EMAIL_INVALID_FORMAT ->
                        viewModel.setFailMessage(getString(R.string.email_invalid))
                    else -> {}
                }
            }
        })

        return binding.root
    }
}