package com.ahmedmatem.android.matura.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.AccountActivity
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentAccountBinding
import com.ahmedmatem.android.matura.prizesystem.PrizeSetup
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : BaseFragment() {

    override lateinit var viewModel: AccountViewModel
    private lateinit var _googleSignInClient: GoogleSignInClient

    private val loginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                /**
                 * PRIZE SETUP - onLogin
                 *
                 * Only for free distribution in all app versions after success login
                 * enqueue work request to setup prize.
                 */
                PrizeSetup.onLogin(requireContext())

                viewModel.login()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        _googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        val binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val loginBtn: Button = binding.loginBtn

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            loginResultLauncher.launch(intent)
        }

        viewModel.onLogout.observe(viewLifecycleOwner, Observer { logout ->
            if (logout) {
                // Logout from google
                _googleSignInClient.signOut().addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("DEBUG", "Google signOut was complete successfully.")
                    } else {
                        Log.d("DEBUG", "Google signOut exception ${it.exception?.message}.")
                    }
                })
                // Logout from Facebook
                Firebase.auth.signOut()
            }
        })

        return binding.root
    }
}