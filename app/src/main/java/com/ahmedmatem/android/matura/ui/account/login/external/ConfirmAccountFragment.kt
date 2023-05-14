package com.ahmedmatem.android.matura.ui.account.login.external

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentConfirmAccountBinding

class ConfirmAccountFragment : BaseFragment() {
    override lateinit var viewModel: ConfirmAccountViewModel
    private val args: ConfirmAccountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this, ConfirmAccountViewModel.Factory(args))[ConfirmAccountViewModel::class.java]

        val binding = FragmentConfirmAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.loginAttemptResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                activity?.apply {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

        return binding.root
    }
}