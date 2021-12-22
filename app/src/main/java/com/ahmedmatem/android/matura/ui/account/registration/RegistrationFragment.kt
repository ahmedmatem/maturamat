package com.ahmedmatem.android.matura.ui.account.registration

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentRegistrationBinding

class RegistrationFragment : BaseFragment() {
    override lateinit var viewModel: RegistrationViewModel
    private val args: RegistrationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, RegistrationViewModel.Factory(requireContext(), args))
            .get(RegistrationViewModel::class.java)

        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.onLoginComplete.observe(viewLifecycleOwner){ complete ->
            if (complete) {
                activity?.apply {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }

        return binding.root
    }
}