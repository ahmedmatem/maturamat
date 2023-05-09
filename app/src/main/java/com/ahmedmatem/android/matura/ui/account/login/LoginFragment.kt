package com.ahmedmatem.android.matura.ui.account.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.infrastructure.PasswordOptions
import com.ahmedmatem.android.matura.infrastructure.afterTextChanged
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {

    private lateinit var auth: FirebaseAuth
    override lateinit var viewModel: LoginViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    // Google external login launcher for result
    private val externalLoginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                val idToken = account.idToken!!
                // Send idToken to the Server for validation
                viewModel.validateIdToken(idToken, ExternalLoginProvider.Google.name)
            } catch (exc: ApiException) {
                // TODO: ApiException not implemented yet
//                Log.w("WARN", "handleSignInResult:error", exc)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginSuccessState.collect {isSuccess ->
                    Log.d("DEBUG2", "onCreate: Collect loginSuccessState")
                    if(isSuccess) {
                        with(requireActivity()) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                }
            }
        }

        // Google SignIn Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("DEBUG", "Usher is logged in.")
        } else {
            Log.d("DEBUG", "Usher isn't logged in.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.username.afterTextChanged {
            viewModel.afterUsernameChanged(it)
        }

        binding.password.afterTextChanged {
            viewModel.afterPasswordChanged(it)
        }

        binding.passwordValidationMessage.text =
            getString(R.string.password_validation_message, PasswordOptions.REQUIRED_LENGTH)


        // External login
        viewModel.externalLoginFlow.observe(viewLifecycleOwner) { provider ->
            provider?.let {
                when (it) {
                    ExternalLoginProvider.Google -> {
                        externalLoginResultLauncher.launch(googleSignInClient.signInIntent)
                    }
                    else -> {}
                }
                viewModel.onExternalLoginComplete()
            }
        }

        return binding.root
    }
}