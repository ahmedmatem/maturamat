package com.ahmedmatem.android.matura.ui.auth.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.ui.auth.login.LoginResult.*

class LoginFragment : BaseFragment() {

    override lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, LoginViewModel.Factory(requireContext()))
                .get(LoginViewModel::class.java)

        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.username.observe(viewLifecycleOwner, Observer {
            viewModel.validateLoginButtonEnableState()
        })

        viewModel.password.observe(viewLifecycleOwner, Observer {
            viewModel.validateLoginButtonEnableState()
        })

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                SUCCESS -> {
                    activity?.apply {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
                GENERIC_ERROR -> TODO("Implement GENERIC_ERROR login result case")
                NETWORK_ERROR -> TODO("Implement NETWORK_ERROR login result case")
            }
        })

        return binding.root
    }
}