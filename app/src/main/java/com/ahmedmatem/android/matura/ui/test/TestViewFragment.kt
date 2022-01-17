package com.ahmedmatem.android.matura.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.databinding.FragmentTestViewBinding

class TestViewFragment : BaseFragment() {
    override lateinit var viewModel: TestViewViewModel
    private val args: TestViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            TestViewViewModel.Factory(requireContext(), args)
        ).get(TestViewViewModel::class.java)

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
//                add<TestBottomSheetFragment>(R.id.bottomSheetContainer)
                add(R.id.bottomSheetContainer, TestBottomSheetFragment.newInstance(viewModel.test))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestViewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.testWebView.settings.javaScriptEnabled = true
        // todo: here add javascript interface
        binding.testWebView.loadUrl(viewModel.url)

        return binding.root
    }
}