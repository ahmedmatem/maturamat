package com.ahmedmatem.android.matura.ui.account.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : BaseFragment() {

    override lateinit var viewModel: LoginViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    private val externalLoginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                val idToken = account.idToken
//                Log.w("DEBUG", "idToken $idToken")
                // Send idToken to the Server for validation
                viewModel.validateIdToken(idToken, ExternalLoginProvider.Google.name)
            } catch (exc: ApiException) {
                Log.w("WARN", "handleSignInResult:error", exc)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
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

        viewModel.loginAttemptResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                activity?.apply {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

        // External login
        viewModel.externalLoginFlow.observe(viewLifecycleOwner) { provider ->
            provider?.let {
                when (it) {
                    ExternalLoginProvider.Google -> {
                        externalLoginResultLauncher.launch(googleSignInClient.signInIntent)
                    }
                    ExternalLoginProvider.Facebook -> {
                    }
                }
                viewModel.onExternalLoginComplete()
            }
        }

        return binding.root
    }
}