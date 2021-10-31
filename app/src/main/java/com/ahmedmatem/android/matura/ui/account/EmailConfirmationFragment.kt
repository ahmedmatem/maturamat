package com.ahmedmatem.android.matura.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.databinding.FragmentEmailConfirmationBinding

class EmailConfirmationFragment : BaseFragment() {
    override lateinit var viewModel: EmailConfirmationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(EmailConfirmationViewModel::class.java)

        val binding = FragmentEmailConfirmationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }
}