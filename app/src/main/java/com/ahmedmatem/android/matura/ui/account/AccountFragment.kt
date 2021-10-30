package com.ahmedmatem.android.matura.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.AuthActivity
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentAccountBinding
import com.ahmedmatem.android.matura.prizesystem.PrizeSetup
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnLoginWorker

class AccountFragment : BaseFragment() {

    override lateinit var viewModel: AccountViewModel

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
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        val binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val loginBtn: Button = binding.loginBtn

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            loginResultLauncher.launch(intent)
        }

        return binding.root
    }
}