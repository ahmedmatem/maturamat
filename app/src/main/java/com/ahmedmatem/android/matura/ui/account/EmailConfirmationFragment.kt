package com.ahmedmatem.android.matura.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentEmailConfirmationBinding

class EmailConfirmationFragment : BaseFragment() {
    override lateinit var viewModel: EmailConfirmationViewModel
    private val args: EmailConfirmationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this, EmailConfirmationViewModel.Factory(requireContext(), args)).get(
                EmailConfirmationViewModel::class.java
            )

        val binding = FragmentEmailConfirmationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.navigateToEmailClient.observe(viewLifecycleOwner) { intent ->
            intent?.let {
                startActivity(intent)
            }
        }

        return binding.root
    }
}