package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.hideBottomNavigation
import com.ahmedmatem.android.matura.databinding.FragmentNewTestBinding

class NewTestFragment : BaseFragment() {
    override lateinit var viewModel: NewTestViewModel
    private val args: NewTestFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewTestViewModel::class.java)

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
        val binding = FragmentNewTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.newTest.settings.javaScriptEnabled = true
        // todo: here add javascript interface
        binding.newTest.loadUrl(args.url)

        return binding.root
    }
}