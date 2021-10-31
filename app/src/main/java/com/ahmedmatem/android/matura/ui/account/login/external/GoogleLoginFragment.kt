package com.ahmedmatem.android.matura.ui.account.login.external

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ahmedmatem.android.matura.databinding.FragmentExternalLoginBinding

class GoogleLoginFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentExternalLoginBinding.inflate(inflater, container, false)

        val webView = binding.externalLoginWebView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://maturamat.com/account/login")

        return binding.root
    }


}