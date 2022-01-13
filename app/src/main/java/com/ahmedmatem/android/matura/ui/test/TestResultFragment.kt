package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.databinding.FragmentTestResultBinding

class TestResultFragment: Fragment() {
    private val args: TestResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTestResultBinding.inflate(inflater, container, false)

        binding.testResultWebView.loadUrl(args.url)

        return binding.root
    }
}