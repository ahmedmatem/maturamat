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
import com.ahmedmatem.android.matura.prizesystem.worker.SetupPrizeOnLoginWorker

class AccountFragment : BaseFragment() {

    override lateinit var viewModel: AccountViewModel

    private val loginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                /**
                 * Only for free distribution in all app versions setup Prize after success login
                 */
                if (BuildConfig.FLAVOR_distribution == "free") {
                    val setupPrizeOnLoginRequest =
                        OneTimeWorkRequest.from(SetupPrizeOnLoginWorker::class.java)
                    WorkManager.getInstance(requireContext()).enqueue(setupPrizeOnLoginRequest)
                }
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

        val loginBtn: Button = binding.button2
        val registerBtn: Button = binding.button3

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            loginResultLauncher.launch(intent)
        }

        return binding.root
    }
}