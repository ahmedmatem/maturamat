package com.ahmedmatem.android.matura.ui.auth.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.prizesystem.worker.SetupPrizeOnLoginWorker
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

        viewModel.loginAttemptResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                SUCCESS -> {
                    activity?.apply {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
                GENERIC_ERROR -> TODO("Not yet implemented")
                NETWORK_ERROR -> TODO("Not yet implemented")
            }
        })

        return binding.root
    }
}