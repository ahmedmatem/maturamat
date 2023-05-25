package com.ahmedmatem.android.matura.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.AccountActivity
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentAccountBinding
import com.ahmedmatem.android.matura.infrastructure.FlavorDistribution
import com.ahmedmatem.android.matura.prizesystem.PrizeWorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : BaseFragment() {

    override lateinit var viewModel: AccountViewModel
    private lateinit var _googleSignInClient: GoogleSignInClient

    private lateinit var binding: FragmentAccountBinding

    private val loginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, ": In - Login activity result.")
                // Refresh test list for user in local database if necessary.
                viewModel.refreshUserTestListIfNecessary()

                // Apply only for FREE distribution.
                if (BuildConfig.FLAVOR_distribution == FlavorDistribution.FREE) {
                    // Prize setup
                    PrizeWorkManager(requireContext()).setup()
                    viewModel.refreshTotalCoin()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        _googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val loginBtn = binding.loginBtn
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

        viewModel.totalCoin.observe(viewLifecycleOwner) {
            binding.totalCoins.text = it.toString()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: starts")
        viewModel.updateAccountActive()
        viewModel.refreshTotalCoin()
    }

    companion object {
        const val TAG = "AccountFragment"
    }
}