package com.ahmedmatem.android.matura.ui.auth.login.external

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ahmedmatem.android.matura.databinding.FragmentExternalLoginBinding

class GoogleLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExternalLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.externalLoginWebView.settings.javaScriptEnabled = true
        binding.externalLoginWebView.loadUrl("https://www.google.com/")

        return binding.root
    }
}