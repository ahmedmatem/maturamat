package com.ahmedmatem.android.matura.ui.auth.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.ui.auth.login.LoginResult.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginFragment : BaseFragment() {

    override lateinit var viewModel: LoginViewModel

    private val googleSignInResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.handleGoogleAccount(task)
        }

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

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

        viewModel.externalLogin.observe(viewLifecycleOwner, Observer { provider ->
            provider?.let {
                when (it) {
                    ExternalLoginProvider.Google -> {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInResultLauncher.launch(signInIntent)
                    }
                    ExternalLoginProvider.Facebook -> {
                        TODO("Facebook sign in not implemented yet.")
                    }
                }
            }
        })

        return binding.root
    }
}