package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.databinding.FragmentResumeTestBinding

class ResumeTestFragment: Fragment() {

    private val args: ResumeTestFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<TestBottomSheetFragment>(R.id.bottomSheetContainer)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResumeTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.resumeTest.settings.javaScriptEnabled = true
        binding.resumeTest.loadUrl(args.url)

        return binding.root
    }
}