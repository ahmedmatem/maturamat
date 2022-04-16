package com.ahmedmatem.android.matura.ui.account.login

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentLoginBinding
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : BaseFragment() {

    private lateinit var auth: FirebaseAuth
    override lateinit var viewModel: LoginViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    // Facebook callbackManager to handle login response
    private lateinit var callbackManager: CallbackManager

    // Google external login launcher
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

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        // Register callback for facebook login result
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("DEBUG", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("DEBUG", "facebook:onError:${error.message}")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("DEBUG", "Facebook:onSuccess:$result")
                    handleFacebookAccessToken(result.accessToken)
                }
            })

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

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("DEBUG", "signInWithCredential:success")
                val user = auth.currentUser
                Log.d("DEBUG", "user: $user ")
//                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("WARN", "signInWithCredential:failure", task.exception)
                Toast.makeText(
                    requireContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
//                updateUI(null)
            }
        })
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
                        LoginManager.getInstance()
                            .logInWithReadPermissions(this, listOf(PUBLIC_PROFILE))
                    }
                }
                viewModel.onExternalLoginComplete()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val EMAIL = "email"
        const val PUBLIC_PROFILE = "public_profile"
        const val AUTH_TYPE = "request"
    }
}